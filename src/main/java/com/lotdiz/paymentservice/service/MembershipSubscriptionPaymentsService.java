package com.lotdiz.paymentservice.service;

import com.lotdiz.paymentservice.dto.request.MembershipInfoForAssignRequestDto;
import com.lotdiz.paymentservice.dto.request.PaymentsInfoForKakoaPayRequestDto;
import com.lotdiz.paymentservice.dto.response.KakaoPayApproveResponseDto;
import com.lotdiz.paymentservice.dto.response.KakaoPayReadyResponseDto;
import com.lotdiz.paymentservice.entity.MembershipSubscriptionPayments;
import com.lotdiz.paymentservice.respository.MembershipSubscriptionPaymentsRepository;
import com.lotdiz.paymentservice.service.client.MemberClientService;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class MembershipSubscriptionPaymentsService {
  private final Logger logger =
      LoggerFactory.getLogger(MembershipSubscriptionPaymentsService.class);

  private final MembershipSubscriptionPaymentsRepository membershipSubscriptionPaymentsRepository;
  private final MembershipSubscriptionService membershipSubscriptionService;
  private final MemberClientService memberClientService;

  @Value("${my.admin}")
  private String ADMIN_KEY;

  private KakaoPayReadyResponseDto kakaoReady;

  @Transactional
  public Long ready(PaymentsInfoForKakoaPayRequestDto paymentsDto) {
    Long membershipSubscriptionId =
        membershipSubscriptionService.create(paymentsDto.getMembershipId());
    String prefix = "LOT_PARTNER";
    String random = UUID.randomUUID().toString();
    String partnerOrderId = prefix + "_ORDER_" + random;
    String partnerUserId = prefix + "_USER_" + random;

    byte[] orderBytes = partnerOrderId.getBytes();
    byte[] userBytes = partnerUserId.getBytes();
    String encodedPartnerOrderId = Base64.getEncoder().encodeToString(orderBytes);
    String encodedPartnerUserId = Base64.getEncoder().encodeToString(userBytes);

    MultiValueMap<String, Object> payParams = new LinkedMultiValueMap<>();
    payParams.add("cid", "TC0ONETIME"); // test payments use cid as "TC0ONETIME"
    payParams.add("partner_order_id", partnerOrderId);
    payParams.add("partner_user_id", partnerUserId);
    payParams.add("item_name", paymentsDto.getItemName());
    payParams.add("quantity", paymentsDto.getQuantity());
    payParams.add("total_amount", paymentsDto.getTotalAmount());
    payParams.add("tax_free_amount", paymentsDto.getTaxFreeAmount());
    payParams.add(
        "approval_url",
        "http://localhost:8085/api/payments/success/"
            + paymentsDto.getMembershipId()
            + "/"
            + membershipSubscriptionId
            + "/"
            + encodedPartnerOrderId
            + "/"
            + encodedPartnerUserId);
    payParams.add("cancel_url", "http://localhost:8085/api/payments/cancel");
    payParams.add("fail_url", "http://localhost:8085/api/payments/fail");

    HttpEntity<Map> requestEntity = new HttpEntity<>(payParams, this.getHeaders());

    RestTemplate template = new RestTemplate();
    String url = "https://kapi.kakao.com/v1/payment/ready";
    kakaoReady = template.postForObject(url, requestEntity, KakaoPayReadyResponseDto.class);
    logger.info(kakaoReady.getNext_redirect_pc_url());
    return membershipSubscriptionId;
  }

  @Transactional
  public void approve(
      String pgToken,
      String membershipId,
      String membershipSubscriptionId,
      String encodedPartnerOrderId,
      String encodedPartnerUserId) {
    byte[] decodedOrderBytes = Base64.getDecoder().decode(encodedPartnerOrderId);
    byte[] decodedUserBytes = Base64.getDecoder().decode(encodedPartnerUserId);
    String partnerOrderId = new String(decodedOrderBytes);
    String partnerUserId = new String(decodedUserBytes);

    MultiValueMap<String, Object> payParams = new LinkedMultiValueMap<>();
    payParams.add("cid", "TC0ONETIME");
    payParams.add("tid", kakaoReady.getTid());
    payParams.add("partner_order_id", partnerOrderId);
    payParams.add("partner_user_id", partnerUserId);
    payParams.add("pg_token", pgToken);

    HttpEntity<Map> requestEntity = new HttpEntity<>(payParams, this.getHeaders());
    String url = "https://kapi.kakao.com/v1/payment/approve";
    RestTemplate template = new RestTemplate();
    KakaoPayApproveResponseDto kakaoApprove =
        template.postForObject(url, requestEntity, KakaoPayApproveResponseDto.class);

    MembershipSubscriptionPayments memberSubscriptionPayments =
        MembershipSubscriptionPayments.create(membershipSubscriptionId, kakaoApprove);

    MembershipSubscriptionPayments membershipSubscriptionPayments =
        membershipSubscriptionPaymentsRepository.save(memberSubscriptionPayments);

    MembershipInfoForAssignRequestDto membershipAssignDto =
        MembershipInfoForAssignRequestDto.builder()
            .membershipId(Long.parseLong(membershipId))
            .createdAt(membershipSubscriptionPayments.getCreatedAt())
            .build();

    memberClientService.assignCreatedAt(membershipAssignDto);
  }

  private HttpHeaders getHeaders() {
    HttpHeaders headers = new HttpHeaders();

    String auth = "KakaoAK " + ADMIN_KEY;
    headers.set("Authorization", auth);
    headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
    return headers;
  }
}
