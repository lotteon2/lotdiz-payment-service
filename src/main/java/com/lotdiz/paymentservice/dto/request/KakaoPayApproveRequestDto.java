package com.lotdiz.paymentservice.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoPayApproveRequestDto {
    private Long fundingId;
    private String tid;
    private String pgToken;
    private String partnerOrderId;
    private String partnerUserId;
}
