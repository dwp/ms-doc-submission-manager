package uk.gov.dwp.health.pip.document.submission.manager.service;

public interface CloudWatchMetricsService {

  void incrementMetric(String metricName);

  void incrementSubmissionFailureMetric();

}
