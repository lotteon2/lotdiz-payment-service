package com.lotdiz.paymentservice.service;

import com.lotdiz.paymentservice.dto.request.KakaopayInfoForCreateRequestDto;
import com.lotdiz.paymentservice.dto.request.MembershipInfoForAssignRequestDto;
import com.lotdiz.paymentservice.dto.request.PaymentsInfoForKakoaPayRequestDto;
import com.lotdiz.paymentservice.dto.response.KakaoPayApproveResponseDto;
import com.lotdiz.paymentservice.dto.response.KakaoPayReadyForMemberResponseDto;
import com.lotdiz.paymentservice.dto.response.KakaoPayReadyResponseDto;
import com.lotdiz.paymentservice.entity.Kakaopay;
import com.lotdiz.paymentservice.entity.MembershipSubscription;
import com.lotdiz.paymentservice.entity.MembershipSubscriptionPayments;
import com.lotdiz.paymentservice.respository.MembershipSubscriptionPaymentsRepository;
import com.lotdiz.paymentservice.service.client.MemberClientService;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class MembershipSubscriptionPaymentsService {

  private final MembershipSubscriptionPaymentsRepository membershipSubscriptionPaymentsRepository;
  private final MembershipSubscriptionService membershipSubscriptionService;
  private final MemberClientService memberClientService;
  private final KakaopayService kakaopayService;

  @Value("${my.admin}")
  private String ADMIN_KEY;

  @Value("${endpoint.apigateway-service}")
  private String APIGATEWAY_SERVICE_URL;

  @Transactional
  public KakaoPayReadyForMemberResponseDto ready(PaymentsInfoForKakoaPayRequestDto paymentsDto) {
    Long membershipSubscriptionId =
        membershipSubscriptionService.create(paymentsDto.getMembershipId());
    String prefix = "LOT_PARTNER";
    String random = UUID.randomUUID().toString();
    String partnerOrderId = prefix + "_ORDER_" + random;
    String partnerUserId = prefix + "_USER_" + random;

    byte[] orderBytes = partnerOrderId.getBytes();
    String encodedPartnerOrderId = Base64.getEncoder().encodeToString(orderBytes);

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
        String.format("%s/payment-service/api/payments/success/", APIGATEWAY_SERVICE_URL)
            + paymentsDto.getMembershipId()
            + "/"
            + membershipSubscriptionId
            + "/"
            + encodedPartnerOrderId);
    payParams.add("cancel_url", String.format("%s/payment-service/api/payments/cancel", APIGATEWAY_SERVICE_URL));
    payParams.add("fail_url", String.format("%s/payment-service/api/payments/fail", APIGATEWAY_SERVICE_URL));

    HttpEntity<Map> requestEntity = new HttpEntity<>(payParams, this.getHeaders());

    RestTemplate template = new RestTemplate();
    String url = "https://kapi.kakao.com/v1/payment/ready";

    KakaoPayReadyResponseDto kakaoReady =
        template.postForObject(url, requestEntity, KakaoPayReadyResponseDto.class);

    log.info(kakaoReady.getNext_redirect_pc_url());
    KakaopayInfoForCreateRequestDto kakaoInfoDto =
        KakaopayInfoForCreateRequestDto.builder()
            .kakaopayPartnerOrderId(partnerOrderId)
            .kakaopayPartnerUserId(partnerUserId)
            .kakaopayTid(kakaoReady.getTid())
            .kakaopayCid("TC0ONETIME")
            .build();
    kakaopayService.createKakaopay(kakaoInfoDto);

    KakaoPayReadyForMemberResponseDto kakaoPayReadyForMemberDto =
        KakaoPayReadyForMemberResponseDto.builder()
            .next_redirect_pc_url(kakaoReady.getNext_redirect_pc_url())
            .membershipSubscriptionId(membershipSubscriptionId)
            .build();
    return kakaoPayReadyForMemberDto;
  }

  @Transactional
  public void approve(
      String pgToken,
      String membershipId,
      String membershipSubscriptionId,
      String encodedPartnerOrderId) {
    byte[] decodedOrderBytes = Base64.getDecoder().decode(encodedPartnerOrderId);
    String partnerOrderId = new String(decodedOrderBytes);

    Kakaopay kakaopay = kakaopayService.findKakaopayByKakaopayOrderId(partnerOrderId);
    MultiValueMap<String, Object> payParams = new LinkedMultiValueMap<>();
    payParams.add("cid", kakaopay.getKakaopayCid());
    payParams.add("tid", kakaopay.getKakaopayTid());
    payParams.add("partner_order_id", partnerOrderId);
    payParams.add("partner_user_id", kakaopay.getKakaopayPartnerUserId());
    payParams.add("pg_token", pgToken);

    HttpEntity<Map> requestEntity = new HttpEntity<>(payParams, this.getHeaders());
    String url = "https://kapi.kakao.com/v1/payment/approve";
    RestTemplate template = new RestTemplate();
    KakaoPayApproveResponseDto kakaoApprove =
        template.postForObject(url, requestEntity, KakaoPayApproveResponseDto.class);

    MembershipSubscription membershipSubscription = membershipSubscriptionService.findByMembershipSubscriptionId(Long.valueOf(membershipSubscriptionId));
    MembershipSubscriptionPayments membershipSubscriptionPayments =
        create(membershipSubscription, kakaoApprove, kakaopay);

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

  public MembershipSubscriptionPayments create(
      MembershipSubscription membershipSubscription, KakaoPayApproveResponseDto kakaoApprove, Kakaopay kakaopay) {
    MembershipSubscriptionPayments memberSubscriptionPayments =
        MembershipSubscriptionPayments.create(membershipSubscription, kakaoApprove, kakaopay);

    return membershipSubscriptionPaymentsRepository.save(memberSubscriptionPayments);
  }
}
