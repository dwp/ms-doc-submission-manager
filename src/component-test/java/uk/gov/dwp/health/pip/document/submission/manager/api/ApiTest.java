package uk.gov.dwp.health.pip.document.submission.manager.api;

import ch.qos.logback.classic.Level;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

public class ApiTest {
  static RequestSpecification requestSpec;
  private static boolean loggingReconfigured = false;

  @BeforeAll
  public static void setup() {
    reduceLoggerOutput();
    RestAssured.baseURI = getEnv("HOST", "http://localhost");
    RestAssured.port = Integer.parseInt(getEnv("PORT", "9945"));
    RestAssured.defaultParser = Parser.JSON;

    requestSpec =
        new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .addFilter(new AllureRestAssured())
            .build();
  }

  private static void reduceLoggerOutput() {
    if (!loggingReconfigured) {
      loggingReconfigured = true;
      reduceLoggerOutput("org.springframework.data.convert.CustomConversions");
      reduceLoggerOutput("org.mongodb.driver.client");
      reduceLoggerOutput("org.mongodb.driver.cluster");
      reduceLoggerOutput("org.mongodb.driver.connection");
    }
  }

  private static void reduceLoggerOutput(final String name) {
    ((ch.qos.logback.classic.Logger)LoggerFactory.getLogger(name)).setLevel(Level.ERROR);
  }

  protected Response postRequest(String path, Object bodyPayload) {
    return given().spec(requestSpec).body(bodyPayload).when().post(path);
  }

  protected Response getRequest(String path) {
    return given().spec(requestSpec).when().get(path);
  }

  private static String getEnv(String name, String defaultValue) {
    String env = System.getenv(name);
    return env == null ? defaultValue : env;
  }
}
