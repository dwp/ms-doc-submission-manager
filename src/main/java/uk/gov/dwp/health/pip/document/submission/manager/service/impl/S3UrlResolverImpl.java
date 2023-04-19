package uk.gov.dwp.health.pip.document.submission.manager.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import uk.gov.dwp.health.pip.document.submission.manager.service.S3UrlResolver;

@Service
public class S3UrlResolverImpl implements S3UrlResolver {

  private final S3Utilities utilities;

  @Autowired
  public S3UrlResolverImpl(final S3Utilities utilities) {
    this.utilities = utilities;
  }

  @Override
  public String resolve(final String bucket, final String key) {
    if (bucket == null || bucket.isBlank()) {
      throw new IllegalStateException("Bucket name must provided");
    }
    if (key == null || key.isBlank()) {
      throw new IllegalStateException("Key must provided");
    }
    return utilities
        .getUrl(GetUrlRequest.builder().bucket(bucket).key(key).build())
        .toExternalForm();
  }
}
