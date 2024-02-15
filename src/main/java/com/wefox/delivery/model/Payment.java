package com.wefox.delivery.model;

import com.wefox.delivery.enums.PaymentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy= GenerationType.TABLE)
    private String paymentId;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "accountId")
    private int accountId;
    @Column(name = "payment_type")
    private PaymentType paymentType;
    @Column(name = "credit_card")
    private String creditCard;
    @Column(name = "amount")
    private double amount;
    @Column(name = "created_at")
    private Timestamp createdAt;

    public Payment(int accountId, PaymentType paymentType) {
        this.accountId = accountId;
        this.paymentType = paymentType;
    }
}
