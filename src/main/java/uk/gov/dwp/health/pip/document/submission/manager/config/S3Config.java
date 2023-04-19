package uk.gov.dwp.health.pip.document.submission.manager.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.S3Utilities;
import uk.gov.dwp.health.pip.document.submission.manager.config.properties.S3ConfigProperties;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@Configuration
public class S3Config {

  @Bean
  public S3Client s3Client(final S3ConfigProperties props) throws URISyntaxException {
    log.info("Create S3 client bean");
    return props.getEndpointOverride() != null
        ? S3Client.builder()
            .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
            .endpointOverride(new URI(props.getEndpointOverride()))
            .serviceConfiguration(S3Configuration.builder().build())
            .region(Region.of(props.getAwsRegion()))
            .build()
        : S3Client.builder()
            .serviceConfiguration(S3Configuration.builder().build())
            .region(Region.of(props.getAwsRegion()))
            .build();
  }

  @Bean
  @Lazy
  public S3Utilities s3Utilities(S3Client s3Client) {
    return s3Client.utilities();
  }
}
