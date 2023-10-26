package com.lotdiz.paymentservice.entity;

import static javax.persistence.GenerationType.IDENTITY;

import com.lotdiz.paymentservice.dto.response.KakaoPayApproveResponseDto;
import com.lotdiz.paymentservice.entity.common.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "membership_subscription_payments")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembershipSubscriptionPayments extends BaseEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "membership_subscription_payments_unique_id")
  private Long membershipSubscriptionPaymentsUniqueId;

  @Column(name = "membership_subscription_id", nullable = false)
  private Long membershipSubscriptionId;

  @Column(name = "membership_subscription_payments_actual_amount", nullable = false)
  private Integer membershipSubscriptionPaymentsActualAmount;

  @Column(name = "membership_subscription_payments_type", nullable = false)
  @Builder.Default
  private String membershipSubscriptionPaymentsType = "CARD";

  @Column(name = "membership_subscription_payments_status", nullable = false)
  private String membershipSubscriptionPaymentsStatus;

  @OneToOne
  @JoinColumn(name = "kakaopay_id")
  private Kakaopay kakaopay;

  public static MembershipSubscriptionPayments create(
      String membershipSubscriptionId, KakaoPayApproveResponseDto kakaoApprove) {
    return MembershipSubscriptionPayments.builder()
        .membershipSubscriptionId(Long.valueOf(membershipSubscriptionId))
        .membershipSubscriptionPaymentsActualAmount(kakaoApprove.getAmount().getTotal())
        .membershipSubscriptionPaymentsStatus("진행")
        .build();
  }
}
