package com.wefox.delivery.offlinepayment.repository;

import com.wefox.delivery.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfflinePaymentRepository extends JpaRepository<Payment,Integer> {
}
