package com.lotdiz.paymentservice.controller.restcontroller;

import com.lotdiz.paymentservice.dto.response.KakaoPayApproveResponseDto;
import com.lotdiz.paymentservice.dto.response.KakaoPayReadyResponseDto;
import com.lotdiz.paymentservice.service.MembershipSubscriptionPaymentsService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MembershipSubscriptionPaymentsRestController {
    private final MembershipSubscriptionPaymentsService membershipSubscriptionPaymentsService;
    private Logger logger = LoggerFactory.getLogger(MembershipSubscriptionPaymentsRestController.class);

    @GetMapping("/membership/payments/ready")
    public @ResponseBody KakaoPayReadyResponseDto kakaoPayReady(@RequestParam Map<String, Object> params) {
        KakaoPayReadyResponseDto readyResponseDto = membershipSubscriptionPaymentsService.ready(params);
        logger.info("readyResponseDto: " + readyResponseDto);
        return readyResponseDto;
    }

    @GetMapping("/payments/success/{membershipId}")
    public String kakaoPayApprove(@RequestParam("pg_token") String pgToken, @PathVariable("membershipId") String membershipId) {
        KakaoPayApproveResponseDto approveResponseDto = membershipSubscriptionPaymentsService.approve(pgToken, membershipId);
        return "카카오 결제 완료";
    }
}
