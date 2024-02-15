package com.wefox.delivery.onlinepayment.service;

import com.wefox.delivery.enums.ErrorType;
import com.wefox.delivery.logs.repository.LogRepository;
import com.wefox.delivery.model.ErrorLog;
import com.wefox.delivery.model.Payment;
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
import java.util.Collections;
import java.util.Properties;

@Service
public class PaymentConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentConsumer.class);
    @Value("${spring.kafka.consumer.bootstrap-servers}")
    public static String BOOTSTRAP_SERVERS_VALUE;
    @Value("${spring.kafka.consumer.group-id}")
    public static String GROUP_ID_VALUE;
    @Value("${spring.kafka.consumer.key-deserializer}")
    public static String KEY_DESERIALIZER_VALUE;
    @Value("${spring.kafka.consumer.value-deserializer}")
    public static String VALUE_DESERIALIZER_VALUE;

    OnlinePaymentRepository onlinePaymentRepository;
    LogRepository logRepository;

    @Autowired
    public PaymentConsumer(OnlinePaymentRepository onlinePaymentRepository,LogRepository logRepository) {
        this.onlinePaymentRepository = onlinePaymentRepository;
        this.logRepository=logRepository;
    }

    @KafkaListener(topics = Constant.TOPIC_ONLINE)
    public boolean validateConsumedData(String message) {
        LOGGER.info("Start processing the data");
        Properties props = new Properties();
        props.put(Constant.BOOTSTRAP_SERVERS, BOOTSTRAP_SERVERS_VALUE);
        props.put(Constant.GROUP_ID, GROUP_ID_VALUE);
        props.put(Constant.KEY_DESERIALIZER, KEY_DESERIALIZER_VALUE);
        props.put(Constant.VALUE_DESERIALIZER, VALUE_DESERIALIZER_VALUE);
        Consumer<String, Payment> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(Constant.TOPIC_ONLINE));
        String paymentId="";
        try {
            while (true) {
                ConsumerRecords<String, Payment> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, Payment> record : records) {
                    Payment paymentData = record.value();
                    paymentId=paymentData.getPaymentId();
                    boolean isValid = validatePayment(paymentData);
                    if (isValid) {
                        return true;
                    }
                }
            }

        }
        catch (Exception ee)
        {
            if(ee instanceof SQLException)
            {
                ErrorLog errorLog=new ErrorLog();
                errorLog.setPaymentId(paymentId);
                errorLog.setErrorType(ErrorType.DATABASE);
                errorLog.setErrorDescription(ee.getMessage());
                logRepository.save(errorLog);
            }
            else if(ee instanceof RuntimeException)
            {
                ErrorLog errorLog=new ErrorLog();
                errorLog.setPaymentId(paymentId);
                errorLog.setErrorType(ErrorType.OTHER);
                errorLog.setErrorDescription(ee.getMessage());
                logRepository.save(errorLog);
            }
            else
            {
                ErrorLog errorLog=new ErrorLog();
                errorLog.setPaymentId(paymentId);
                errorLog.setErrorType(ErrorType.NETWORK);
                errorLog.setErrorDescription(ee.getMessage());
                logRepository.save(errorLog);
            }

        }
        finally {
            consumer.close();
        }
        return false;
    }


    private boolean validatePayment(Payment paymentData) {
        if(paymentData.getCreditCard().length()==Constant.CREDIT_CARD_ALLOWED_LENGTH)
          return true;
     return false;
    }
}
