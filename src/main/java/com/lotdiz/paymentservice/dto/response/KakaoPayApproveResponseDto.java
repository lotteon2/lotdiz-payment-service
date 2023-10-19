package com.lotdiz.paymentservice.dto.response;

import com.lotdiz.paymentservice.dto.response.Amount;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KakaoPayApproveResponseDto {
  private String aid; // 요청 고유번호
  private String tid; // 결제 고유번호
  private String cid; // 가맹점 코드
  private String sid; // 정기 결제용 id
  private String partner_order_id; // 가맹점 주문번호
  private String partner_user_id; // 가맹점 회원
  private String payment_method_type; // 결제수단
  private String item_name;
  private String item_code;
  private Integer quantity;
  private String created_at;
  private String approved_at;
  private Amount amount;
}
