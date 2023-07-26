package uk.gov.dwp.health.pip.document.submission.manager.config;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import uk.gov.dwp.health.pip.document.submission.manager.config.properties.CloudWatchProperties;

@Configuration
public class CloudWatchClientConfiguration {
  final Logger logger = LoggerFactory.getLogger(CloudWatchClientConfiguration.class);

  private final CloudWatchProperties cloudWatchProperties;

  public CloudWatchClientConfiguration(final CloudWatchProperties cloudWatchProperties) {
    this.cloudWatchProperties = cloudWatchProperties;
  }

  @Primary
  @Bean
  public AmazonCloudWatch cloudWatchClient() {
    final String override = cloudWatchProperties.getEndpointOverride();
    final AmazonCloudWatchClientBuilder standard = AmazonCloudWatchClientBuilder.standard();
    if (override == null || override.trim().isEmpty()) {
      logger.debug("AWS config no endpoint");
      standard.withRegion(cloudWatchProperties.getAwsRegion());
    } else {
      logger.warn("Localstack mode");
      AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration(
          override, cloudWatchProperties.getAwsRegion()
      );

      final BasicAWSCredentials credentials = new BasicAWSCredentials(
          cloudWatchProperties.getAwsAccessKey(),
          cloudWatchProperties.getAwsSecretAccessKey()
      );
      final AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(
          credentials
      );
      standard.withEndpointConfiguration(endpoint)
          .withCredentials(credentialsProvider);
    }
    return standard.build();
  }
}
