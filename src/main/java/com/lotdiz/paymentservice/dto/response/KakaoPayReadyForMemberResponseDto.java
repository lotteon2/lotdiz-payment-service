package com.lotdiz.paymentservice.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class KakaoPayReadyForMemberResponseDto {
  private String next_redirect_pc_url;
  private Long membershipSubscriptionId;
}
