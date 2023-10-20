package com.lotdiz.paymentservice.service;

import com.lotdiz.paymentservice.dto.request.KakaoPayReadyRequestDto;
import com.lotdiz.paymentservice.dto.response.KakaoPayApproveResponseDto;
import com.lotdiz.paymentservice.dto.response.KakaoPayReadyResponseDto;
import com.lotdiz.paymentservice.entity.FundingPayments;
import com.lotdiz.paymentservice.entity.Kakaopay;
import com.lotdiz.paymentservice.entity.PaymentsStatus;
import com.lotdiz.paymentservice.repository.FundingPaymentsRepository;
import com.lotdiz.paymentservice.repository.KakaopayRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class FundingPaymentsService {
  private final FundingPaymentsRepository fundingPaymentsRepository;
  private final KakaopayRepository kakaopayRepository;

  @Value("${my.admin}")
  private String ADMIN_KEY;
  @Value("${my.cid}")
  private String CID;

  public KakaoPayReadyResponseDto payReady(KakaoPayReadyRequestDto kakaoPayReadyRequestDto) {

    String prefix = "LOT_PARTNER";
    String random = UUID.randomUUID().toString();
    String partnerOrderId = prefix + "_ORDER_" + random;
    String partnerUserId = prefix + "_USER_" + random;

    MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
    parameters.add("cid", CID);
    parameters.add("partner_order_id", partnerOrderId);
    parameters.add("partner_user_id", partnerUserId);
    parameters.add("item_name", kakaoPayReadyRequestDto.getItem_name());
    parameters.add("quantity", String.valueOf(kakaoPayReadyRequestDto.getQuantity()));
    parameters.add("total_amount", String.valueOf(kakaoPayReadyRequestDto.getTotal_amount()));
    parameters.add("tax_free_amount", String.valueOf(kakaoPayReadyRequestDto.getTax_free_amount()));
    parameters.add(
        "approval_url",
        "http://localhost:8085/api/payments/completed"
            + "/"
            + partnerOrderId
            + "/"
            + kakaoPayReadyRequestDto.getFunding_id());
    parameters.add("cancel_url", "http://localhost:8085/api/payments/cancel");
    parameters.add("fail_url", "http://localhost:8085/api/payments/fail");

    HttpEntity<MultiValueMap<String, String>> requestEntity =
        new HttpEntity<>(parameters, this.getHeaders());

    RestTemplate template = new RestTemplate();
    String url = "https://kapi.kakao.com/v1/payment/ready";
    KakaoPayReadyResponseDto readyResponse =
        template.postForObject(url, requestEntity, KakaoPayReadyResponseDto.class);

    Kakaopay kakaopay =
        Kakaopay.builder()
            .kakaoPayPartnerOrderId(partnerOrderId)
            .kakaopayPartnerUserId(partnerUserId)
            .kakaopayCid(CID)
            .kakaopayTid(readyResponse.getTid())
            .build();
    kakaopayRepository.save(kakaopay);

    return readyResponse;
  }

  public void payApprove(String pgToken, String partnerOrderId, Long fundingId) {
    Kakaopay kakaopay = kakaopayRepository.findByKakaoPayPartnerOrderId(partnerOrderId);

    MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
    parameters.add("cid", kakaopay.getKakaopayCid());
    parameters.add("tid", kakaopay.getKakaopayTid());
    parameters.add("partner_order_id", kakaopay.getKakaoPayPartnerOrderId());
    parameters.add("partner_user_id", kakaopay.getKakaopayPartnerUserId());
    parameters.add("pg_token", pgToken);

    HttpEntity<MultiValueMap<String, String>> requestEntity =
        new HttpEntity<>(parameters, this.getHeaders());

    RestTemplate template = new RestTemplate();
    String url = "https://kapi.kakao.com/v1/payment/approve";

    KakaoPayApproveResponseDto approveResponse =
        template.postForObject(url, requestEntity, KakaoPayApproveResponseDto.class);

    // FundingPayments에 정보 저장
    FundingPayments fundingPayments =
        FundingPayments.builder()
            .fundingId(fundingId)
            .kakaopay(kakaopay)
            .fundingPaymentsType(approveResponse.getPayment_method_type())
            .fundingPaymentsActualAmount(Long.valueOf(approveResponse.getAmount().getTotal()))
            .fundingPaymentsStatus(PaymentsStatus.COMPLETED)
            .build();

    fundingPaymentsRepository.save(fundingPayments);
  }

  @NotNull
  private HttpHeaders getHeaders() {
    HttpHeaders headers = new HttpHeaders();

    String auth = "KakaoAK " + ADMIN_KEY;
    headers.set("Authorization", auth);
    headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
    return headers;
  }
}