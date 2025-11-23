package com.rodem.homework.payment.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentFailureRepository {
    PaymentFailure save(PaymentFailure failure);

    Page<PaymentFailure> findAll(Pageable pageable);
}
