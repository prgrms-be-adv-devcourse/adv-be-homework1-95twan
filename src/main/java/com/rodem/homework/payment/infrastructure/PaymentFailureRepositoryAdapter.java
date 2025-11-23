package com.rodem.homework.payment.infrastructure;

import com.rodem.homework.payment.domain.Payment;
import com.rodem.homework.payment.domain.PaymentFailure;
import com.rodem.homework.payment.domain.PaymentFailureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentFailureRepositoryAdapter implements PaymentFailureRepository {

    private final PaymentFailureJpaRepository paymentFailureJpaRepository;

    @Override
    public PaymentFailure save(PaymentFailure failure) {
        return paymentFailureJpaRepository.save(failure);
    }

    @Override
    public Page<PaymentFailure> findAll(Pageable pageable) {
        return paymentFailureJpaRepository.findAll(pageable);
    }
}
