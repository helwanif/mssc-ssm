package com.example.msscssm.services;

import com.example.msscssm.domain.Payment;
import com.example.msscssm.domain.PaymentEvent;
import com.example.msscssm.domain.PaymentState;
import com.example.msscssm.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaymentServiceImplTest {

    @Autowired
    PaymentService paymentService;

    @Autowired
    PaymentRepository paymentRepository;

    Payment payment;
    @BeforeEach
    void setUp() {
        payment = Payment.builder().amount(new BigDecimal("12.99")).build();
    }

    @Transactional
    @Test
    void preAuth() {
        Payment savedPayment = paymentService.newPayment(payment);

        paymentService.preAuth(savedPayment.getId());
        StateMachine<PaymentState, PaymentEvent> sm = paymentService.preAuth(payment.getId());

        Payment preAuthedPayment = paymentRepository.getById(savedPayment.getId());
        System.out.println(sm.getState().getId());
        System.out.println(preAuthedPayment);
    }

    @Transactional
     @Test
    void auth() {
        Payment savedPayment = paymentService.newPayment(payment);

        paymentService.preAuth(savedPayment.getId());
        StateMachine<PaymentState, PaymentEvent> sm = paymentService.preAuth(payment.getId());

        Payment preAuthedPayment = paymentRepository.getById(savedPayment.getId());
        System.out.println(preAuthedPayment);

        Payment authedPayment = paymentRepository.getById(savedPayment.getId());

        sm = paymentService.authorizePayment(authedPayment.getId());
        System.out.println(authedPayment);

    }
}