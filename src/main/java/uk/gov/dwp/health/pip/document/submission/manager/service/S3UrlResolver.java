package uk.gov.dwp.health.pip.document.submission.manager.service;

@FunctionalInterface
public interface S3UrlResolver {

  String resolve(final String bucket, final String key);
}
