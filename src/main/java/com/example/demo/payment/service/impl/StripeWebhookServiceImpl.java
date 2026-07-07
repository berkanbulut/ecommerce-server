package com.example.demo.payment.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.order.entity.Order;
import com.example.demo.order.entity.OrderStatus;
import com.example.demo.order.entity.PaymentStatus;
import com.example.demo.order.repository.OrderRepository;
import com.example.demo.payment.config.StripeProperties;
import com.example.demo.payment.service.StripeWebhookService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StripeWebhookServiceImpl implements StripeWebhookService {

    private final StripeProperties stripeProperties;
    private final OrderRepository orderRepository;

    public StripeWebhookServiceImpl(
            StripeProperties stripeProperties,
            OrderRepository orderRepository
    ) {
        this.stripeProperties = stripeProperties;
        this.orderRepository = orderRepository;
    }

    @Override
    public void handleWebhook(String payload, String signature) {
        Event event;

        try {
            event = Webhook.constructEvent(
                    payload,
                    signature,
                    stripeProperties.getWebhookSecret()
            );
        } catch (SignatureVerificationException e) {
            throw new BadRequestException("Invalid Stripe signature!");
        }

        switch (event.getType()) {
            case "checkout.session.completed" -> handleCheckoutSessionCompleted(event);

            default -> System.out.println("Unhandled Stripe Event: " + event.getType());
        }
    }

    private void handleCheckoutSessionCompleted(Event event) {
        Session session = (Session) event
                .getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new BadRequestException("Stripe session could not be deserialized!"));

        String orderIdValue = session.getMetadata().get("orderId");

        if (orderIdValue == null) {
            throw new BadRequestException("Order id not found in Stripe metadata!");
        }

        Long orderId = Long.valueOf(orderIdValue);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> NotFoundException.of("Order", orderId));

        if (order.getPaymentStatus() == PaymentStatus.PAID) {
            return;
        }

        String paymentIntentId = session.getPaymentIntent();

        order.markPaymentPaid(
                "STRIPE",
                paymentIntentId
        );

        order.updateOrderStatus(OrderStatus.CONFIRMED);

        System.out.println("Order paid successfully. Order Id: " + orderId);
    }
}