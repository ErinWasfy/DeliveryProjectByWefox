package com.wefox.delivery.onlinepayment.onlinepaymentcontroller;

import com.wefox.delivery.model.Payment;
import com.wefox.delivery.onlinepayment.service.PaymentConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExternalThirdPartyController {
    PaymentConsumer paymentConsumer;

    @Autowired
    public ExternalThirdPartyController(PaymentConsumer paymentConsumer) {
        this.paymentConsumer = paymentConsumer;
    }
    @GetMapping("/payment")
    public ResponseEntity<String> validatePayment(String message)
    {
        if(paymentConsumer.validateConsumedData(message))
       return new ResponseEntity<String>("Valid Payment", HttpStatus.OK);
        return new ResponseEntity<String>("Invalid Payment", HttpStatus.BAD_REQUEST);
    }
}
