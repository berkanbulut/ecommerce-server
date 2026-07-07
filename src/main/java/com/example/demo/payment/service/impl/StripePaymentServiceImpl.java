package com.example.demo.payment.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.order.entity.Order;
import com.example.demo.order.entity.PaymentMethod;
import com.example.demo.order.entity.PaymentStatus;
import com.example.demo.order.repository.OrderRepository;
import com.example.demo.payment.config.StripeProperties;
import com.example.demo.payment.dto.StripeCheckoutSessionResponseDto;
import com.example.demo.payment.service.StripePaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StripePaymentServiceImpl implements StripePaymentService {

    private final OrderRepository orderRepository;
    private final StripeProperties stripeProperties;

    public StripePaymentServiceImpl(
            OrderRepository orderRepository,
            StripeProperties stripeProperties
    ) {
        this.orderRepository = orderRepository;
        this.stripeProperties = stripeProperties;
    }

    @Override
    public StripeCheckoutSessionResponseDto createCheckoutSession(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> NotFoundException.of("Order", orderId));

        if (order.getPaymentMethod() != PaymentMethod.CARD) {
            throw new BadRequestException("Only card payments can use Stripe checkout!");
        }

        if (order.getPaymentStatus() != PaymentStatus.PENDING) {
            throw new BadRequestException("Order payment is not pending!");
        }

        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(stripeProperties.getSuccessUrl() + "?orderId=" + order.getId())
                    .setCancelUrl(stripeProperties.getCancelUrl() + "?orderId=" + order.getId())
                    .setClientReferenceId(String.valueOf(order.getId()))
                    .putMetadata("orderId", String.valueOf(order.getId()))
                    .putMetadata("orderNumber", order.getOrderNumber())
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency(order.getCurrency().toLowerCase())
                                                    .setUnitAmount(
                                                            order.getGrandTotal()
                                                                    .multiply(java.math.BigDecimal.valueOf(100))
                                                                    .longValue()
                                                    )
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Order " + order.getOrderNumber())
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

            Session session = Session.create(params);

            order.attachStripeSession(session.getId());

            return new StripeCheckoutSessionResponseDto(
                    session.getId(),
                    session.getUrl()
            );

        } catch (StripeException e) {
            throw new BadRequestException("Stripe checkout session could not be created!");
        }
    }
}