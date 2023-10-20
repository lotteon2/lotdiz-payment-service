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
  private Long funding_id;
  private String item_name;
  private int quantity;
  private int total_amount;
  private int tax_free_amount;
}
