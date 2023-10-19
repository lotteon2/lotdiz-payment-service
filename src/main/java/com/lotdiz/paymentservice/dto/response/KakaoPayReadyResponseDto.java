package com.lotdiz.paymentservice.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KakaoPayReadyResponseDto {
  private String tid;
  private String next_redirect_pc_url;
}
