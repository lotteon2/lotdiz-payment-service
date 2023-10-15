package com.lotdiz.paymentservice.controller.restcontroller;

import com.lotdiz.paymentservice.controller.service.MembershipSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MembershipSubscriptionRestController {
    private final MembershipSubscriptionService membershipSubscriptionService;
}
