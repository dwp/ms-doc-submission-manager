package uk.gov.dwp.health.pip.document.submission.manager.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.dwp.health.pip.document.submission.manager.utils.RequestPartition;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.TimeZone;

@Configuration
@Slf4j
public class AppConfig {

  @Bean
  public Base64.Decoder decoder() {
    return Base64.getDecoder();
  }

  @Bean
  public Base64.Encoder encoder() {
    return Base64.getEncoder();
  }

  @Bean
  public ObjectMapper objectMapper() {
    return JsonMapper.builder()
        .addModule(new ParameterNamesModule())
        .addModule(new Jdk8Module())
        .addModule(new JavaTimeModule())
        .build();
  }

  @Bean
  public DateFormat dateFormatter() {
    log.info("Create date formatter bean with date format [{}]", "yyyy-MM-dd");
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    format.setTimeZone(TimeZone.getDefault());
    return format;
  }

  @Bean
  public RequestPartition requestPartition(
      @Value("${drs.max.disk.size.kb.per.batch:-1}") int vol,
      @Value("${drs.max.number.file.per.batch:-1}") int fc) {
    log.info("Create request partition bean disk size kb [{}] file per batch [{}]", vol, fc);
    return new RequestPartition(vol, fc);
  }
}
