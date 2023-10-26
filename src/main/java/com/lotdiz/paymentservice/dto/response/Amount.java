package com.lotdiz.paymentservice.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Amount {
  private Integer total;
  private Integer tax_free;
  private Integer vat;
  private Integer point;
  private Integer discount;
}
