package com.lotdiz.paymentservice.service.client;

import com.lotdiz.paymentservice.dto.request.MembershipInfoForAssignRequestDto;
import java.lang.reflect.Member;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberClientService {
  private final MemberServiceClient memberServiceClient;

  public void assignCreatedAt(MembershipInfoForAssignRequestDto membershipAssignDto) {
    memberServiceClient.assignCreatedAt(membershipAssignDto);
  }
}
