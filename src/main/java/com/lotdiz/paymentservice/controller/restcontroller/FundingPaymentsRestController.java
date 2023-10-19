package com.lotdiz.paymentservice.controller.restcontroller;

import com.lotdiz.paymentservice.dto.request.KakaoPayReadyRequestDto;
import com.lotdiz.paymentservice.dto.response.KakaoPayReadyResponseDto;
import com.lotdiz.paymentservice.service.FundingPaymentsService;
import com.lotdiz.paymentservice.utils.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FundingPaymentsRestController {
  private final FundingPaymentsService fundingPaymentsService;

  @PostMapping("/funding/payments/ready")
  public ResponseEntity<SuccessResponse> createPayments(
      @RequestBody KakaoPayReadyRequestDto readyRequestDto) {

    KakaoPayReadyResponseDto readyResponseDto = fundingPaymentsService.payReady(readyRequestDto);
    log.info(readyResponseDto.getNext_redirect_pc_url());
    return ResponseEntity.ok()
        .body(
            SuccessResponse.builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message(HttpStatus.OK.name())
                .detail("카카오페이 결제 준비 성공")
                .data(readyResponseDto.getNext_redirect_pc_url())
                .build());
  }

//  @GetMapping("/payments/cancel")
//  public ResponseEntity<SuccessResponse> cancelPayments(
//
//  )
@GetMapping("/payments/completed")
public String payCompleted(@RequestParam("pg_token") String pgToken, @RequestParam("tid") String tid){

  log.info(pgToken + "," + tid);
  return
}

}
