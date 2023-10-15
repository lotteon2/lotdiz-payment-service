package com.lotdiz.paymentservice.respository;

import com.lotdiz.paymentservice.entity.MembershipSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipSubscriptionRepository extends JpaRepository<MembershipSubscription, Long> {

}
