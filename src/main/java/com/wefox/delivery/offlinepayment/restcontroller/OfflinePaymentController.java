package com.wefox.delivery.offlinepayment.restcontroller;

import com.wefox.delivery.model.Account;
import com.wefox.delivery.model.Payment;
import com.wefox.delivery.offlinepayment.service.PaymentConsumer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.channels.AcceptPendingException;
import java.sql.SQLDataException;

@RestController
@RequestMapping("/offline-pay")
public class OfflinePaymentController {
  com.wefox.delivery.offlinepayment.service.PaymentConsumer paymentConsumer;
  public OfflinePaymentController(PaymentConsumer paymentConsumer)
  {
      this.paymentConsumer=paymentConsumer;
  }
  @PostMapping("/post")
    public ResponseEntity<String> createOfflinePayment(@RequestBody Payment payment, @RequestBody Account account) throws SQLDataException, IOException {
      this.paymentConsumer.saveConsumedData(payment,account);
      return new ResponseEntity<String>("Payment Created", HttpStatus.CREATED);
  }
}
