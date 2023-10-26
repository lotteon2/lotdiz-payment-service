package com.lotdiz.paymentservice.service;

import com.lotdiz.paymentservice.entity.MembershipSubscription;
import com.lotdiz.paymentservice.respository.MembershipSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MembershipSubscriptionService {
  private final MembershipSubscriptionRepository membershipSubscriptionRepository;

  public Long create(String membershipId) {
    MembershipSubscription membershipSubscription =
        MembershipSubscription.builder().membershipId(Long.parseLong(membershipId)).build();
    return membershipSubscriptionRepository
        .save(membershipSubscription)
        .getMembershipSubscriptionId();
  }
}
