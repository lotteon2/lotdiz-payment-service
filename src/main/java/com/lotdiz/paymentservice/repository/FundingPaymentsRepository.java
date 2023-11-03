package com.lotdiz.paymentservice.repository;

import com.lotdiz.paymentservice.entity.FundingPayments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FundingPaymentsRepository extends JpaRepository<FundingPayments, Long> {
    FundingPayments findByFundingId(Long fundingId);
}
