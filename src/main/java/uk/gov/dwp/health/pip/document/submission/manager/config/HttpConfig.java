package uk.gov.dwp.health.pip.document.submission.manager.config;

import com.amazonaws.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class HttpConfig {

  private final Logger log = LoggerFactory.getLogger(HttpConfig.class);

  @Bean
  @Qualifier(value = "fileUploadRestTemplate")
  public RestTemplate restTemplateForMultipartFile() {
    log.info("Create file-upload rest template bean");
    return new RestTemplateBuilder()
        .defaultHeader("Content-Type", MediaType.MULTIPART_FORM_DATA_VALUE)
        .errorHandler(
            new ResponseErrorHandler() {
              final Set<HttpStatus> exemptedRespCodes =
                  Stream.of(HttpStatus.NOT_ACCEPTABLE).collect(Collectors.toSet());

              @Override
              public boolean hasError(ClientHttpResponse response) throws IOException {
                return !exemptedRespCodes.contains(response.getStatusCode());
              }

              @Override
              public void handleError(ClientHttpResponse response) throws IOException {
                if (!exemptedRespCodes.contains(response.getStatusCode())) {
                  log.error(
                      "Response error {}, {}",
                      response.getStatusCode().value(),
                      IOUtils.toString(response.getBody()));
                }
              }
            })
        .build();
  }

  @Bean
  @Qualifier(value = "entityRestTemplate")
  public RestTemplate restTemplateForEntity() {
    log.info("Create entity rest template bean");
    return new RestTemplateBuilder()
        .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        .build();
  }
}
