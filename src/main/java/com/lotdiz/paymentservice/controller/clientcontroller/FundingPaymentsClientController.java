package com.lotdiz.paymentservice.controller.clientcontroller;

import com.lotdiz.paymentservice.dto.request.KakaoPayApproveRequestDto;
import com.lotdiz.paymentservice.service.FundingPaymentsService;
import com.lotdiz.paymentservice.utils.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FundingPaymentsClientController {
  private final FundingPaymentsService fundingPaymentsService;

  @PostMapping("/funding/payments/approval")
  public ResponseEntity<SuccessResponse> payApprove(
      @RequestBody KakaoPayApproveRequestDto approveRequestDto) {
    fundingPaymentsService.payApprove(approveRequestDto);

    // funding-service로 응답.
    return ResponseEntity.ok()
        .body(
            SuccessResponse.builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.name())
                .detail("카카오페이 결제 승인 성공")
                .build());
  }
}
