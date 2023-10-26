package com.lotdiz.paymentservice.controller.restcontroller;

import com.lotdiz.paymentservice.dto.request.PaymentsInfoForKakoaPayRequestDto;
import com.lotdiz.paymentservice.dto.response.ResultDataResponse;
import com.lotdiz.paymentservice.service.MembershipSubscriptionPaymentsService;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MembershipSubscriptionPaymentsRestController {
  private final MembershipSubscriptionPaymentsService membershipSubscriptionPaymentsService;
  private Logger logger =
      LoggerFactory.getLogger(MembershipSubscriptionPaymentsRestController.class);

  @PostMapping("/membership/payments/ready")
  public ResultDataResponse<Map<String, Long>> kakaoPayReady(
      @RequestBody PaymentsInfoForKakoaPayRequestDto paymentsDto) {
    Long membershipSubscriptionId = membershipSubscriptionPaymentsService.ready(paymentsDto);
    Map<String, Long> map = new HashMap<>();
    map.put("membershipSubscriptionId", membershipSubscriptionId);

    return new ResultDataResponse<>(String.valueOf(HttpStatus.OK.value()), HttpStatus.OK.name(), "카카오페이 준비 요청 성공", map);
  }

  @GetMapping(
      "/payments/success/{membershipId}/{membershipSubscriptionId}/{encodedPartnerOrderId}/{encodedPartnerUserId}")
  public ResultDataResponse<Object> kakaoPayApprove(
      @RequestParam("pg_token") String pgToken,
      @PathVariable("membershipId") String membershipId,
      @PathVariable("membershipSubscriptionId") String membershipSubscriptionId,
      @PathVariable("encodedPartnerOrderId") String encodedPartnerOrderId,
      @PathVariable("encodedPartnerUserId") String encodedPartnerUserId) {
    membershipSubscriptionPaymentsService.approve(
        pgToken,
        membershipId,
        membershipSubscriptionId,
        encodedPartnerOrderId,
        encodedPartnerUserId);

    return new ResultDataResponse<>(
        String.valueOf(HttpStatus.OK.value()), HttpStatus.OK.name(), "카카오페이 최종 결제 성공", null);
  }
}
