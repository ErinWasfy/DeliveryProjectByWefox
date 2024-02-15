package com.wefox.delivery.model;

import com.wefox.delivery.enums.ErrorType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorLog {

  private String paymentId;
  private ErrorType errorType;
  private String errorDescription;

    public ErrorLog() {
    }
}
