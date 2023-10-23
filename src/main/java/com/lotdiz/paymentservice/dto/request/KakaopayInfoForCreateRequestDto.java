package com.lotdiz.paymentservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaopayInfoForCreateRequestDto {
    private String kakaopayPartnerOrderId;
    private String kakaopayPartnerUserId;
    private String kakaopayTid;
    private String kakaopayCid;
}
