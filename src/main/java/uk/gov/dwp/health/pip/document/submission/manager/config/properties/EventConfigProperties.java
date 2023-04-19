package uk.gov.dwp.health.pip.document.submission.manager.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@Validated
@Configuration
@ConfigurationProperties("drs.event")
public class EventConfigProperties {
  @NotBlank private String outboundBatchUploadQueue;
  private String version;
  @NotBlank private String incomingRoutingKey;
  @NotBlank private String queueName;
}
