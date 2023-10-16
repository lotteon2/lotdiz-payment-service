package com.lotdiz.paymentservice.respository;

import com.lotdiz.paymentservice.entity.MembershipSubscriptionPayments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipSubscriptionPaymentsRepository extends JpaRepository<MembershipSubscriptionPayments, Long> {}
