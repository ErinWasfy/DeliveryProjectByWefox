package com.wefox.delivery.onlinepayment.repository;

import com.wefox.delivery.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnlinePaymentRepository extends JpaRepository<Payment,Integer> {
}
