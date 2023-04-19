package uk.gov.dwp.health.pip.document.submission.manager.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class S3UrlResolverImplTest {

  @InjectMocks private S3UrlResolverImpl cut;
  @Mock private S3Utilities utilities;
  @Captor private ArgumentCaptor<GetUrlRequest> argumentCaptor;

  @ParameterizedTest
  @NullAndEmptySource
  void testNullOrEmptyBucketThrowsIllegalStateException(String bucket) {
    assertThrows(IllegalStateException.class, () -> cut.resolve(bucket, "key-file-ref"));
  }

  @Test
  void testBlankBucketThrowsIllegalStateException() {
    String bucket = "  ";
    assertThrows(IllegalStateException.class, () -> cut.resolve(bucket, "key-file-ref"));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @EmptySource
  void testNullOrEmptyKeyThrowsIllegalStateException(String key) {
    assertThrows(IllegalStateException.class, () -> cut.resolve("s3-bucket-name", key));
  }

  @Test
  void testBlankKeyThrowsIllegalStateException() {
    String key = "  ";
    assertThrows(IllegalStateException.class, () -> cut.resolve("s3-bucket-name", key));
  }

  @Test
  void testResolveUrlOfGivenBucketAndKey() {
    URL url = mock(URL.class);
    when(url.toExternalForm()).thenReturn("http://s3/pip-bucket/test.txt");
    when(utilities.getUrl(any(GetUrlRequest.class))).thenReturn(url);
    String actual = cut.resolve("pip-bucket", "test.txt");
    verify(utilities).getUrl(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue().bucket()).isEqualTo("pip-bucket");
    assertThat(argumentCaptor.getValue().key()).isEqualTo("test.txt");
    assertThat(actual).isEqualTo("http://s3/pip-bucket/test.txt");
  }
}
