package com.lotdiz.paymentservice.controller.restcontroller;

import com.lotdiz.paymentservice.dto.request.KakaoPayReadyRequestDto;
import com.lotdiz.paymentservice.dto.response.KakaoPayReadyResponseDto;
import com.lotdiz.paymentservice.service.FundingPaymentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
  public KakaoPayReadyResponseDto payReady(@RequestBody KakaoPayReadyRequestDto readyRequestDto) {

    return fundingPaymentsService.payReady(readyRequestDto);
  }
}
