package uk.gov.dwp.health.pip.document.submission.manager.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.path.json.mapper.factory.Jackson2ObjectMapperFactory;

import java.lang.reflect.Type;

public class RestAssuredConfiguration {

  public static void configureObjectMapper(){
    RestAssured.config = io.restassured.config.RestAssuredConfig.config().objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory(
        new Jackson2ObjectMapperFactory() {
          @Override
          public ObjectMapper create(Type cls, String charset) {
            return JsonMapper.builder()
                .addModule(new ParameterNamesModule())
                .addModule(new Jdk8Module())
                .addModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .build();
          }
        }
    ));
  }
}
