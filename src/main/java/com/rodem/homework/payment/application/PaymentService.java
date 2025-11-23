package com.rodem.homework.payment.application;

import com.rodem.homework.common.ResponseEntity;
import com.rodem.homework.order.application.OrderService;
import com.rodem.homework.order.domain.PurchaseOrder;
import com.rodem.homework.payment.application.dto.PaymentCommand;
import com.rodem.homework.payment.application.dto.PaymentFailCommand;
import com.rodem.homework.payment.application.dto.PaymentFailureInfo;
import com.rodem.homework.payment.application.dto.PaymentInfo;
import com.rodem.homework.payment.client.TossPaymentClient;
import com.rodem.homework.payment.client.dto.TossPaymentResponse;
import com.rodem.homework.payment.domain.Payment;
import com.rodem.homework.payment.domain.PaymentFailure;
import com.rodem.homework.payment.domain.PaymentFailureRepository;
import com.rodem.homework.payment.domain.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentFailureRepository paymentFailureRepository;
    private final OrderService orderService;
    private final TossPaymentClient tossPaymentClient;


    public ResponseEntity<List<PaymentInfo>> findAll(Pageable pageable) {
        Page<Payment> page = paymentRepository.findAll(pageable);
        List<PaymentInfo> payments = page.stream().map(PaymentInfo::from).toList();
        return new ResponseEntity<>(HttpStatus.OK.value(), payments, page.getTotalElements());
    }

    public ResponseEntity<List<PaymentFailureInfo>> findFailAll(Pageable pageable) {
        Page<PaymentFailure> page = paymentFailureRepository.findAll(pageable);
        List<PaymentFailureInfo> paymentFailures = page.stream().map(PaymentFailureInfo::from).toList();
        return new ResponseEntity<>(HttpStatus.OK.value(), paymentFailures, page.getTotalElements());
    }

    public ResponseEntity<PaymentInfo> confirm(PaymentCommand command) {
        PurchaseOrder order = orderService.findById(tossPayment.orderId());

        TossPaymentResponse tossPayment = tossPaymentClient.confirm(command);

        Payment payment = Payment.create(
                tossPayment.paymentKey(),
                tossPayment.orderId(),
                tossPayment.totalAmount()
        );
        LocalDateTime approvedAt = tossPayment.approvedAt() != null ? tossPayment.approvedAt().toLocalDateTime() : null;
        LocalDateTime requestedAt = tossPayment.requestedAt() != null ? tossPayment.requestedAt().toLocalDateTime() : null;
        payment.markConfirmed(tossPayment.method(), approvedAt, requestedAt);

        orderService.markPaid(order);

        Payment saved = paymentRepository.save(payment);

        return new ResponseEntity<>(HttpStatus.CREATED.value(), PaymentInfo.from(saved), 1);
    }

    public ResponseEntity<PaymentFailureInfo> recordFailure(PaymentFailCommand command) {
        PaymentFailure failure = PaymentFailure.from(
                command.orderId(),
                command.paymentKey(),
                command.errorCode(),
                command.errorMessage(),
                command.amount(),
                command.rawPayload()
        );
        PaymentFailure saved = paymentFailureRepository.save(failure);
        return new ResponseEntity<>(HttpStatus.CREATED.value(), PaymentFailureInfo.from(saved), 1);
    }
}
