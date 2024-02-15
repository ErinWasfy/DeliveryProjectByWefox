package com.wefox.delivery.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "account")
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue(strategy= GenerationType.TABLE)
    private int accountId;
    @Column(name = "email")
    private String email;
    @Column(name = "birthdate")
    private Date birthDate;
    @Column(name = "last_payment_date")
    private Date lastPaymentDate;
    @Column(name = "created_at")
    private Timestamp createdAt;

    public Account(int accountId) {
        this.accountId = accountId;
    }
}
