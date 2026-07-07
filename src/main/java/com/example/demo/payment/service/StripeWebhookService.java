package com.example.demo.payment.service;

public interface StripeWebhookService {

    void handleWebhook(String payload, String signature);
}