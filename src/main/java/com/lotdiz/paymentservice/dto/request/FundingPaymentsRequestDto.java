package com.lotdiz.paymentservice.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FundingPaymentsRequestDto {
  private String cid;
  private String partner_order_id;
  private String partner_user_id;
  private String item_name;
  private int quantity;
  private int total_amount;
  private int tax_free_amount;
}
