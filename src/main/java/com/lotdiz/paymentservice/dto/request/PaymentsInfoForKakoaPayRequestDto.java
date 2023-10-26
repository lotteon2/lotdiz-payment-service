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
public class PaymentsInfoForKakoaPayRequestDto {
    private String itemName;
    private String quantity;
    private String totalAmount;
    private String taxFreeAmount;
    private String membershipId;
}
