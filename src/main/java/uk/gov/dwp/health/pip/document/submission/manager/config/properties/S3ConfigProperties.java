package uk.gov.dwp.health.pip.document.submission.manager.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@Validated
@Configuration
@ConfigurationProperties(prefix = "aws.s3")
public class S3ConfigProperties {

  @NotBlank(message = "AWS region required")
  private String awsRegion;

  @NotBlank(message = "S3 bucket name required")
  private String bucket;

  private String endpointOverride;
}
