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
@Table(name = "membership_subscription")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembershipSubscription extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "membership_subscription_id")
    private Long membershipSubscriptionId;

    @Column(name = "membership_id", nullable = false)
    private Long membershipId;
}
