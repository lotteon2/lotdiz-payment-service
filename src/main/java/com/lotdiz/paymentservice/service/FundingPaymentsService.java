package com.lotdiz.paymentservice.service;

import com.lotdiz.paymentservice.dto.request.KakaoPayApproveRequestDto;
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
  @Value("${server.host.front}")
  private String FRONT_URL;

  public KakaoPayReadyResponseDto payReady(KakaoPayReadyRequestDto kakaoPayReadyRequestDto) {

    String prefix = "LOT_PARTNER";
    String random = UUID.randomUUID().toString();
    String partnerOrderId = prefix + "_ORDER_" + random;
    String partnerUserId = prefix + "_USER_" + random;

    MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
    parameters.add("cid", "TC0ONETIME");
    parameters.add("partner_order_id", partnerOrderId);
    parameters.add("partner_user_id", partnerUserId);
    parameters.add("item_name", kakaoPayReadyRequestDto.getItemName());
    parameters.add("quantity", String.valueOf(kakaoPayReadyRequestDto.getQuantity()));
    parameters.add("total_amount", String.valueOf(kakaoPayReadyRequestDto.getTotalAmount()));
    parameters.add("tax_free_amount", String.valueOf(kakaoPayReadyRequestDto.getTaxFreeAmount()));

    // TODO: 서비스 주소로 바꾸기.
    parameters.add(
        "approval_url",
            FRONT_URL+"/payments/approve" + "/" + partnerOrderId + "/" + partnerUserId);
    parameters.add("cancel_url", FRONT_URL+"/payments/cancel");
    parameters.add("fail_url", FRONT_URL+"/payments/fail");

    HttpEntity<MultiValueMap<String, String>> requestEntity =
        new HttpEntity<>(parameters, this.getHeaders());

    RestTemplate template = new RestTemplate();
    String url = "https://kapi.kakao.com/v1/payment/ready";
    KakaoPayReadyResponseDto readyResponse =
        template.postForObject(url, requestEntity, KakaoPayReadyResponseDto.class);

    return readyResponse;
  }

  public void payApprove(
      KakaoPayApproveRequestDto approveRequestDto) {
    MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
    parameters.add("cid", "TC0ONETIME");
    parameters.add("tid", approveRequestDto.getTid());
    parameters.add("partner_order_id", approveRequestDto.getPartnerOrderId());
    parameters.add("partner_user_id", approveRequestDto.getPartnerUserId());
    parameters.add("pg_token", approveRequestDto.getPgToken());

    HttpEntity<MultiValueMap<String, String>> requestEntity =
        new HttpEntity<>(parameters, this.getHeaders());

    RestTemplate template = new RestTemplate();
    String url = "https://kapi.kakao.com/v1/payment/approve";

    KakaoPayApproveResponseDto approveResponse =
        template.postForObject(url, requestEntity, KakaoPayApproveResponseDto.class);

    // kakakopay 테이블에 정보 저장
    Kakaopay kakaopay =
        Kakaopay.builder()
            .kakaopayCid("TC0ONETIME")
            .kakaopayTid(approveRequestDto.getTid())
            .kakaoPayPartnerOrderId(approveRequestDto.getPartnerOrderId())
            .kakaopayPartnerUserId(approveRequestDto.getPartnerUserId())
            .build();
    Kakaopay savedKakaopay = kakaopayRepository.save(kakaopay);

    // FundingPayments에 정보 저장
    FundingPayments fundingPayments =
        FundingPayments.builder()
            .fundingId(approveRequestDto.getFundingId())
            .kakaopay(savedKakaopay)
            .fundingPaymentsActualAmount(Long.valueOf(approveResponse.getAmount().getTotal()))
            .fundingPaymentsType(approveResponse.getPayment_method_type())
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
