package com.lotdiz.paymentservice.entity;

import static javax.persistence.GenerationType.IDENTITY;

import com.lotdiz.paymentservice.entity.common.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
  private Long fundingPaymentsUniqueId;

  @Column(name = "funding_id", nullable = false)
  private Long fundingId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "kakaopay_id")
  private Kakaopay kakaopay;

  @Column(name = "funding_payments_actual_amount", nullable = false)
  private Long fundingPaymentsActualAmount;

  @Column(name = "funding_payments_type", nullable = false)
  @Builder.Default
  private String fundingPaymentsType = "CARD";

  @Enumerated(EnumType.STRING)
  @Column(name = "funding_payments_status", nullable = false)
  private PaymentsStatus fundingPaymentsStatus;
}
