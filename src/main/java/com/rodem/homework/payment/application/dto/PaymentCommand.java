package com.rodem.homework.payment.application.dto;

public record PaymentCommand(
        String paymentKey,
        String orderId,
        Long amount
) {
}
