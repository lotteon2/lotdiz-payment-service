package com.lotdiz.paymentservice.dto.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembershipInfoForAssignRequestDto {
  private Long membershipId;

  private LocalDateTime createdAt;
}
