package com.wefox.delivery.offlinepayment.service;

import com.wefox.delivery.enums.ErrorType;
import com.wefox.delivery.logs.repository.LogRepository;
import com.wefox.delivery.model.Account;
import com.wefox.delivery.model.ErrorLog;
import com.wefox.delivery.model.Payment;
import com.wefox.delivery.offlinepayment.repository.OfflinePaymentRepository;
import com.wefox.delivery.onlinepayment.repository.AccountRepository;
import com.wefox.delivery.onlinepayment.repository.OnlinePaymentRepository;
import com.wefox.delivery.utils.Constant;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;

@Service
public class PaymentConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(com.wefox.delivery.onlinepayment.service.PaymentConsumer.class);
    @Value("${spring.kafka.consumer.bootstrap-servers}")
    public static String BOOTSTRAP_SERVERS_VALUE;
    @Value("${spring.kafka.consumer.group-id}")
    public static String GROUP_ID_VALUE;
    @Value("${spring.kafka.consumer.key-deserializer}")
    public static String KEY_DESERIALIZER_VALUE;
    @Value("${spring.kafka.consumer.value-deserializer}")
    public static String VALUE_DESERIALIZER_VALUE;

    OfflinePaymentRepository offlinePaymentRepository;
    AccountRepository accountRepository;
    LogRepository logRepository;
    @Autowired
    public PaymentConsumer(OfflinePaymentRepository offlinePaymentRepository,AccountRepository accountRepository,LogRepository logRepository) {
        this.offlinePaymentRepository = offlinePaymentRepository;
        this.accountRepository=accountRepository;
        this.logRepository=logRepository;
    }

    @KafkaListener(topics = Constant.TOPIC_OFFLINE)
    public void saveConsumedData(Payment payment, Account account) {
        LOGGER.info("Start processing the data");
        Properties props = new Properties();
        props.put(Constant.BOOTSTRAP_SERVERS, BOOTSTRAP_SERVERS_VALUE);
        props.put(Constant.GROUP_ID, GROUP_ID_VALUE);
        props.put(Constant.KEY_DESERIALIZER, KEY_DESERIALIZER_VALUE);
        props.put(Constant.VALUE_DESERIALIZER, VALUE_DESERIALIZER_VALUE);
        Consumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(Constant.TOPIC_ONLINE));

        try {
            int accountId=this.accountRepository.findById(account.getAccountId()).get().getAccountId();
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    String paymentData = record.value();
                    if(accountId!=0)
                    {
                        Account updateAccount=new Account(accountId);
                         updateAccount.setLastPaymentDate(new Date(System.currentTimeMillis()));
                         this.accountRepository.save(updateAccount);
                        this.offlinePaymentRepository.save(payment);
                    }
                }
            }

        }
        catch (Exception ee)
        {
            if(ee instanceof SQLException)
            {
                ErrorLog errorLog=new ErrorLog();
                errorLog.setPaymentId(payment.getPaymentId());
                errorLog.setErrorType(ErrorType.DATABASE);
                errorLog.setErrorDescription(ee.getMessage());
                logRepository.save(errorLog);
            }
            else if(ee instanceof RuntimeException)
            {
                ErrorLog errorLog=new ErrorLog();
                errorLog.setPaymentId(payment.getPaymentId());
                errorLog.setErrorType(ErrorType.OTHER);
                errorLog.setErrorDescription(ee.getMessage());
                logRepository.save(errorLog);
            }
            else
            {
                ErrorLog errorLog=new ErrorLog();
                errorLog.setPaymentId(payment.getPaymentId());
                errorLog.setErrorType(ErrorType.NETWORK);
                errorLog.setErrorDescription(ee.getMessage());
                logRepository.save(errorLog);
            }

        }

        finally {
            consumer.close();
        }
    }
}
