package com.lotdiz.paymentservice.entity;

import static javax.persistence.GenerationType.IDENTITY;

import com.lotdiz.paymentservice.entity.common.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "kakaopay")
public class Kakaopay extends BaseEntity {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long kakaopayId;

  @Column(name = "kakaopay_partner_order_id", nullable = false)
  private String kakaoPayPartnerOrderId;

  @Column(name = "kakaopay_partner_user_id", nullable = false)
  private String kakaopayPartnerUserId;

  @Column(name = "kakaopay_cid", nullable = false)
  private String kakaopayCid;

  @Column(name = "kakaopay_tid", nullable = false)
  private String kakaopayTid;
}
