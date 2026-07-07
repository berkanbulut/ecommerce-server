package com.example.demo.order.dto;

import com.example.demo.order.entity.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateOrderDto {

    @NotNull(message = "Payment method is required!")
    private PaymentMethod paymentMethod;

    @NotBlank(message = "Full name is required!")
    @Size(max = 150, message = "Full name can be max 150 characters!")
    private String shippingFullName;

    @NotBlank(message = "Phone is required!")
    @Size(max = 30, message = "Phone can be max 30 characters!")
    private String shippingPhone;

    @NotBlank(message = "Address is required!")
    @Size(max = 255, message = "Address can be max 255 characters!")
    private String shippingAddressLine;

    @NotBlank(message = "City is required!")
    @Size(max = 100, message = "City can be max 100 characters!")
    private String shippingCity;

    @NotBlank(message = "Country is required!")
    @Size(max = 100, message = "Country can be max 100 characters!")
    private String shippingCountry;

    @NotBlank(message = "Postal code is required!")
    @Size(max = 30, message = "Postal code can be max 30 characters!")
    private String shippingPostalCode;

    @Size(max = 500, message = "Customer note can be max 500 characters!")
    private String customerNote;

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public String getShippingFullName() { return shippingFullName; }
    public String getShippingPhone() { return shippingPhone; }
    public String getShippingAddressLine() { return shippingAddressLine; }
    public String getShippingCity() { return shippingCity; }
    public String getShippingCountry() { return shippingCountry; }
    public String getShippingPostalCode() { return shippingPostalCode; }
    public String getCustomerNote() { return customerNote; }
}