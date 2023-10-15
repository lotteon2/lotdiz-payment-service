package com.lotdiz.paymentservice.controller.restcontroller;

import com.lotdiz.paymentservice.dto.response.KakaoPayApproveResponseDto;
import com.lotdiz.paymentservice.dto.response.KakaoPayReadyResponseDto;
import com.lotdiz.paymentservice.service.PaymentsService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentsController {
    private final PaymentsService paymentsService;
    private Logger logger = LoggerFactory.getLogger(PaymentsController.class);

    @GetMapping("/membership/payments/ready")
    public @ResponseBody KakaoPayReadyResponseDto kakaoPayReady(@RequestParam Map<String, Object> params) {
        KakaoPayReadyResponseDto readyResponseDto = paymentsService.ready(params);
        logger.info("readyResponseDto: " + readyResponseDto);
        return readyResponseDto;
    }

    @GetMapping("/payments/success")
    public String kakaoPayApprove(@RequestParam("pg_token") String pgToken) {
        KakaoPayApproveResponseDto approveResponseDto = paymentsService.approve(pgToken);
        return "카카오 결제 완료";
    }
}
