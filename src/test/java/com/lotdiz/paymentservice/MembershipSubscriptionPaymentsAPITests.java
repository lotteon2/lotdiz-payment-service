package com.lotdiz.paymentservice;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ResponseBodyExtractionOptions;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import static org.assertj.core.api.Assertions.assertThat;

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
//    http://localhost:8085/api/membership/payments/ready?quantity=4&total_amount=8800&tax_free_amount=0&item_name=%EC%B4%88%EC%BD%94%ED%8C%8C%EC%9D%B4
  }
}
