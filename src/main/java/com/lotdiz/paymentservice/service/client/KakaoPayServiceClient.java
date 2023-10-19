package com.lotdiz.paymentservice.service.client;

import com.lotdiz.paymentservice.dto.request.KakaoPayReadyRequestDto;
import com.lotdiz.paymentservice.dto.response.KakaoPayReadyResponseDto;
import com.lotdiz.paymentservice.utils.SuccessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "kakaoPayServiceClient", url = "https://kapi.kakao.com")
public interface KakaoPayServiceClient {
  @PostMapping("/v1/payment/ready")
  SuccessResponse<KakaoPayReadyResponseDto> kakaoPayReady(
      KakaoPayReadyRequestDto kakaoPayReadyRequestDto);
}
