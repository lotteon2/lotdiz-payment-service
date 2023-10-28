package com.lotdiz.paymentservice.controller.restcontroller;

import com.lotdiz.paymentservice.dto.request.KakaoPayReadyRequestDto;
import com.lotdiz.paymentservice.dto.response.KakaoPayReadyResponseDto;
import com.lotdiz.paymentservice.dto.response.ResultDataResponse;
import com.lotdiz.paymentservice.service.FundingPaymentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class FundingPaymentsRestController {
  private final FundingPaymentsService fundingPaymentsService;

  @ResponseBody
  @PostMapping("/funding/payments/ready")
  public ResponseEntity<ResultDataResponse<KakaoPayReadyResponseDto>> payReady(
      @RequestBody KakaoPayReadyRequestDto readyRequestDto) {

    KakaoPayReadyResponseDto KakaoPayReadyResponseDto =
        fundingPaymentsService.payReady(readyRequestDto);
    return ResponseEntity.ok()
        .body(
            new ResultDataResponse<>(
                String.valueOf(HttpStatus.OK.value()),
                HttpStatus.OK.name(),
                "카카오 페이 준비 요청 성공",
                KakaoPayReadyResponseDto));
  }
}
