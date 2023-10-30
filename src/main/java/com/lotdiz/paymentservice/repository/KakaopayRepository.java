package com.lotdiz.paymentservice.repository;

import com.lotdiz.paymentservice.entity.Kakaopay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KakaopayRepository extends JpaRepository<Kakaopay, Long> {
    Kakaopay findByKakaoPayPartnerOrderId(String partnerOrderId);
}
