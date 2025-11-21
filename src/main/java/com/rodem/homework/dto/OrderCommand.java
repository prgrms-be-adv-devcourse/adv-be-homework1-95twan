package com.rodem.homework.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderCommand(
        UUID productId,
        UUID sellerId,
        UUID memberId,
        BigDecimal amount
) {
}
