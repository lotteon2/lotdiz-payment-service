package com.lotdiz.paymentservice.service;

import com.lotdiz.paymentservice.dto.request.KakaoPayReadyRequestDto;
import com.lotdiz.paymentservice.dto.response.KakaoPayApproveResponseDto;
import com.lotdiz.paymentservice.dto.response.KakaoPayReadyResponseDto;
import com.lotdiz.paymentservice.entity.FundingPayments;
import com.lotdiz.paymentservice.repository.FundingPaymentsRepository;
import com.lotdiz.paymentservice.service.client.KakaoPayServiceClient;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoPayService {
    private final KakaoPayServiceClient kakaoPayServiceClient;
    private final FundingPaymentsRepository fundingPaymentsRepository;

    public KakaoPayReadyResponseDto payReady(KakaoPayReadyRequestDto kakaoPayReadyRequestDto){

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
        parameters.add("cid", kakaoPayReadyRequestDto.getCid());
        parameters.add("partner_order_id", kakaoPayReadyRequestDto.getPartner_order_id());
        parameters.add("partner_user_id", kakaoPayReadyRequestDto.getPartner_user_id());
        parameters.add("item_name", kakaoPayReadyRequestDto.getItem_name());
        parameters.add("quantity", String.valueOf(kakaoPayReadyRequestDto.getQuantity()));
        parameters.add("total_amount", String.valueOf(kakaoPayReadyRequestDto.getTotal_amount()));
        parameters.add("tax_free_amount", String.valueOf(kakaoPayReadyRequestDto.getTax_free_amount()));
        parameters.add("approval_url", "http://localhost:8085/api/payments/completed");
        parameters.add("cancel_url", "http://localhost:8085/api/payments/cancel");
        parameters.add("fail_url", "http://localhost:8085/api/payments/fail");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        log.info("파트너주문아이디:"+ parameters.get("partner_order_id"));
        log.info("파트너회원아이디:"+ parameters.get("partner_user_id"));
        RestTemplate template = new RestTemplate();
        String url = "https://kapi.kakao.com/v1/payment/ready";
        KakaoPayReadyResponseDto readyResponse = template.postForObject(url, requestEntity, KakaoPayReadyResponseDto.class);
        log.info("결재준비 응답객체: " + readyResponse);

        // tid 저장
//        FundingPayments fundingPayments = FundingPayments.builder()
//                .fundingPaymentsTid(readyResponse.getTid())
//                .build();
//        fundingPaymentsRepository.save(fundingPayments);


        return readyResponse;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK 98fead643b84b0ef981ed6b58ce58561");
        headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        return headers;
    }
}
