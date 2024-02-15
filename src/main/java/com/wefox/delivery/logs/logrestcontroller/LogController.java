package com.wefox.delivery.logs.logrestcontroller;

import com.wefox.delivery.logs.service.PaymentLog;
import com.wefox.delivery.model.ErrorLog;
import com.wefox.delivery.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
public class LogController {
    PaymentLog paymentLog;

    @Autowired
    public LogController(PaymentLog paymentLog)
    {
        this.paymentLog=paymentLog;
    }
    @PostMapping
    public  ResponseEntity<ErrorLog> postLog(@RequestBody ErrorLog errorLog)
    {
        this.paymentLog.saveError(errorLog);
        return new ResponseEntity<ErrorLog>(errorLog, HttpStatus.CREATED);
    }
}
