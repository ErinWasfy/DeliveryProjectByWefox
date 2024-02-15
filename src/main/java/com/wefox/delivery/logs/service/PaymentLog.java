package com.wefox.delivery.logs.service;

import com.wefox.delivery.logs.repository.LogRepository;
import com.wefox.delivery.model.ErrorLog;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentLog {
    LogRepository logRepository;

    @Autowired
    public PaymentLog(LogRepository logRepository) {
        this.logRepository = logRepository;
    }
    @Transactional
    public void saveError(ErrorLog errorLog)
    {
        logRepository.save(errorLog);
    }
}
