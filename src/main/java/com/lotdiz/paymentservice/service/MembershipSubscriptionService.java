package com.lotdiz.paymentservice.service;

import com.lotdiz.paymentservice.respository.MembershipSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MembershipSubscriptionService {
    private MembershipSubscriptionRepository membershipSubscriptionRepository;
}
