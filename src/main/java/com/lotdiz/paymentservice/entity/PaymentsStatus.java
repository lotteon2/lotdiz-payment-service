package com.lotdiz.paymentservice.entity;

import lombok.Getter;

@Getter
public enum PaymentsStatus {
  COMPLETED("결제 완료"),
  CANCELED("결제 취소");

  private final String message;

  PaymentsStatus(String message) {
    this.message = message;
  }
}
