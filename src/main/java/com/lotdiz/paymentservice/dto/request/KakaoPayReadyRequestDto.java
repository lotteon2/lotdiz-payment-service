package com.lotdiz.paymentservice.dto.request;

import com.lotdiz.paymentservice.entity.FundingPayments;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoPayReadyRequestDto {
  // 나머지 정보는 결제 서비스에서 생성/수신한다.
  private String itemName;
  private int quantity;
  private int totalAmount;
  private int taxFreeAmount;
}
