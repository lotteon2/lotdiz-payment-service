package com.lotdiz.paymentservice;

import io.restassured.RestAssured;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MembershipSubscriptionPaymentsAPITests {

  @Test
  void contextLoads() {}

  @BeforeEach
  public void setUpTestData() throws SQLException {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = 8085;
  }

  @Test
  @DisplayName("멤버십 가입 결제를 할 수 있다")
  void test1() {
    //
    // 펀딩프렌즈
    // http://localhost:8085/api/membership/payments/ready?quantity=1&total_amount=6900&tax_free_amount=0&item_name=%ED%8E%80%EB%94%A9%ED%94%84%EB%A0%8C%EC%A6%88&membership_id=1
    // 펀딩파트너
    // http://localhost:8085/api/membership/payments/ready?quantity=1&total_amount=9900&tax_free_amount=0&item_name=%ED%8E%80%EB%94%A9%ED%8C%8C%ED%8A%B8%EB%84%88&membership_id=1
  }
}
