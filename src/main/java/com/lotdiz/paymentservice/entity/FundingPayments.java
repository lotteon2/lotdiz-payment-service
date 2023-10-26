package com.lotdiz.paymentservice.entity;

import static javax.persistence.GenerationType.IDENTITY;

import com.lotdiz.paymentservice.entity.common.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "funding_payments")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundingPayments extends BaseEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "funding_payments_unique_id")
  private Long membershipSubscriptionPaymentsUniqueId;

  @Column(name = "funding_id", nullable = false)
  private Long membershipSubscriptionId;

  @Column(name = "funding_payments_actual_amount", nullable = false)
  private Long membershipSubscriptionPaymentsActualAmount;

  @Column(name = "funding_payments_type", nullable = false)
  @Builder.Default
  private String membershipSubscriptionPaymentsType = "CARD";

  @Column(name = "funding_payments_status", nullable = false)
  private String membershipSubscriptionPaymentsStatus;

  @Column(name = "funding_payments_tid", nullable = false)
  private String membershipSubscriptionPaymentsTid; // 결제 고유번호

  @Column(name = "funding_payments_cid", nullable = false)
  private String membershipSubscriptionPaymentsCid; // 가맹점 코드
}
