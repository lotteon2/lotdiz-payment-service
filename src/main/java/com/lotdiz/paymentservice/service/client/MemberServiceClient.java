package com.lotdiz.paymentservice.service.client;

import com.lotdiz.paymentservice.dto.request.MembershipInfoForAssignRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "member-service", url = "http://localhost:8083")
public interface MemberServiceClient {

  @PostMapping("/api/members/membership/assign")
  void assignCreatedAt(@RequestBody MembershipInfoForAssignRequestDto membershipAssignDto);
}
