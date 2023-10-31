package com.lotdiz.paymentservice.controller.restcontroller;

import com.lotdiz.paymentservice.dto.request.KakaoPayReadyRequestDto;
import com.lotdiz.paymentservice.dto.response.KakaoPayReadyResponseDto;
import com.lotdiz.paymentservice.service.FundingPaymentsService;
import com.lotdiz.paymentservice.utils.SuccessResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FundingPaymentsRestController {
  private final FundingPaymentsService fundingPaymentsService;

  @PostMapping("/funding/payments/ready")
  public ResponseEntity<SuccessResponse<Map<String, KakaoPayReadyResponseDto>>> payReady(
      @RequestBody KakaoPayReadyRequestDto readyRequestDto) {

    KakaoPayReadyResponseDto dto = fundingPaymentsService.payReady(readyRequestDto);
    return ResponseEntity.ok()
        .body(
            SuccessResponse.<Map<String, KakaoPayReadyResponseDto>>builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.name())
                .detail("결제 준비 완료")
                .data(Map.of("payReady", dto))
                .build());
  }
}
