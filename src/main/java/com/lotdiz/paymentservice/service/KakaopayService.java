package com.lotdiz.paymentservice.service;

import com.lotdiz.paymentservice.dto.request.KakaopayInfoForCreateRequestDto;
import com.lotdiz.paymentservice.entity.Kakaopay;
import com.lotdiz.paymentservice.repository.KakaopayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaopayService {
  private final KakaopayRepository kakaopayRepository;

  public void createKakaopay(KakaopayInfoForCreateRequestDto kakaoInfoDto) {
    Kakaopay kakaopay =
        Kakaopay.builder()
            .kakaoPayPartnerOrderId(kakaoInfoDto.getKakaopayPartnerOrderId())
            .kakaopayPartnerUserId(kakaoInfoDto.getKakaopayPartnerUserId())
            .kakaopayTid(kakaoInfoDto.getKakaopayTid())
            .kakaopayCid(kakaoInfoDto.getKakaopayCid())
            .build();
    kakaopayRepository.save(kakaopay);
  }

  public Kakaopay findKakaopayByKakaopayOrderId(String partnerOrderId) {
    return kakaopayRepository.findByKakaoPayPartnerOrderId(partnerOrderId);
  }
}
