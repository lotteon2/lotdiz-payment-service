package com.lotdiz.paymentservice.controller.restcontroller;

import com.lotdiz.paymentservice.dto.request.PaymentsInfoForKakoaPayRequestDto;
import com.lotdiz.paymentservice.dto.response.KakaoPayReadyForMemberResponseDto;
import com.lotdiz.paymentservice.dto.response.ResultDataResponse;
import com.lotdiz.paymentservice.service.MembershipSubscriptionPaymentsService;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

  @PostMapping("/membership/payments/ready")
  public ResponseEntity<ResultDataResponse<KakaoPayReadyForMemberResponseDto>> kakaoPayReady(
      @RequestBody PaymentsInfoForKakoaPayRequestDto paymentsDto) {
    KakaoPayReadyForMemberResponseDto kakaoPayReadyForMemberDto =
        membershipSubscriptionPaymentsService.ready(paymentsDto);

    return ResponseEntity.ok()
        .body(
            new ResultDataResponse<>(
                String.valueOf(HttpStatus.OK.value()),
                HttpStatus.OK.name(),
                "카카오 페이 준비 요청 성공",
                kakaoPayReadyForMemberDto));
  }

  @GetMapping("/payments/success/{membershipId}/{membershipSubscriptionId}/{encodedPartnerOrderId}")
  public ResponseEntity<ResultDataResponse<Object>> kakaoPayApprove(
      @RequestParam("pg_token") String pgToken,
      @PathVariable("membershipId") String membershipId,
      @PathVariable("membershipSubscriptionId") String membershipSubscriptionId,
      @PathVariable("encodedPartnerOrderId") String encodedPartnerOrderId,
      HttpServletResponse response)
      throws IOException {
    membershipSubscriptionPaymentsService.approve(
        pgToken, membershipId, membershipSubscriptionId, encodedPartnerOrderId);
    response.sendRedirect("http://localhost:5173");
    return ResponseEntity.ok()
        .body(
            new ResultDataResponse<>(
                String.valueOf(HttpStatus.OK.value()), HttpStatus.OK.name(), "카카오 결제 완료", null));
  }
}
