package com.lotdiz.paymentservice.controller.restcontroller;

import com.lotdiz.paymentservice.dto.request.KakaoPayReadyRequestDto;
import com.lotdiz.paymentservice.dto.response.KakaoPayApproveResponseDto;
import com.lotdiz.paymentservice.dto.response.KakaoPayReadyResponseDto;
import com.lotdiz.paymentservice.entity.FundingPayments;
import com.lotdiz.paymentservice.entity.Kakaopay;
import com.lotdiz.paymentservice.service.FundingPaymentsService;
import com.lotdiz.paymentservice.utils.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class FundingPaymentsRestController {
  private final FundingPaymentsService fundingPaymentsService;

  @ResponseBody
  @PostMapping("/funding/payments/ready")
  public ResponseEntity<SuccessResponse> createPayments(
      @RequestBody KakaoPayReadyRequestDto readyRequestDto) {

    KakaoPayReadyResponseDto readyResponseDto = fundingPaymentsService.payReady(readyRequestDto);
    return ResponseEntity.ok()
        .body(
            SuccessResponse.builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.name())
                .detail("카카오페이 결제 준비 성공")
                .data(readyResponseDto.getNext_redirect_pc_url())
                .build());
  }

  // TODO: ResponseBody로 바꾸기
  @GetMapping("/payments/cancel")
  public String cancelPayments() {
    return "cancel";
  }

  // TODO: ResponseBody로 바꾸기
  @GetMapping("/payments/complete")
  public String completePayments() {
    return "redirect:/complete.html";
  }

  // url 형식은 Kakaopay API 참고함.
  @GetMapping("/payments/completed/{partner_order_id}/{funding_id}")
  public String payCompleted(
      @RequestParam("pg_token") String pgToken,
      @PathVariable("partner_order_id") String partnerOrderId,
      @PathVariable("funding_id") Long fundingId) {
    fundingPaymentsService.payApprove(pgToken, partnerOrderId, fundingId);

    // funding-service로 응답.
    // 현재는 일단 client로 응답.
    return "redirect:/api/payments/complete";
  }
}
