package uk.gov.dwp.health.pip.document.submission.manager.config;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.s3.S3Client;
import uk.gov.dwp.health.pip.document.submission.manager.config.properties.S3ConfigProperties;

import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class S3ConfigTest {

  private static S3ConfigProperties prop;
  private static S3Config cut;

  @BeforeAll
  static void setupSpec() {
    cut = new S3Config();
    prop = mock(S3ConfigProperties.class);
  }

  @Test
  void testCreateAwsS3ClientWithEndpointOverride() throws URISyntaxException {
    when(prop.getAwsRegion()).thenReturn("eu-west2");
    when(prop.getEndpointOverride()).thenReturn("localstack:4527");
    S3Client actual = cut.s3Client(prop);
    assertThat(actual).isNotNull();
  }

  @Test
  void testCreateAwsS3Client() throws URISyntaxException {
    when(prop.getEndpointOverride()).thenReturn(null);
    when(prop.getAwsRegion()).thenReturn("eu-west2");
    S3Config cut = new S3Config();
    S3Client actual = cut.s3Client(prop);
    assertThat(actual).isNotNull();
  }

  @Test
  void testCreateS3UtilsBean() {
    S3Client client = mock(S3Client.class);
    cut.s3Utilities(client);
    verify(client).utilities();
  }
}
