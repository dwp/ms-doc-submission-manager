package uk.gov.dwp.health.pip.document.submission.manager.service.impl;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.StandardUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import uk.gov.dwp.health.pip.document.submission.manager.config.properties.CloudWatchProperties;
import uk.gov.dwp.health.pip.document.submission.manager.service.CloudWatchMetricsService;

@Service
@RequiredArgsConstructor
public class CloudWatchMetricsServiceImpl implements CloudWatchMetricsService {

  private static final String CHANNEL_NAME_STRATEGIC = "strategic";
  private static final String DIMENSION_NAME_CHANNEL = "channel";
  private final AmazonCloudWatch cloudWatchClient;
  private final CloudWatchProperties cloudWatchProperties;
  private final Environment environment;

  private String buildVersion = null;

  @Override
  public void incrementSubmissionFailureMetric() {
    incrementMetric(cloudWatchProperties.getSubmissionFailureMetricName());
  }

  @Override
  public void incrementMetric(final String metricName) {
    final MetricDatum datum = new MetricDatum()
        .withMetricName(metricName)
        .withUnit(StandardUnit.None)
        .withValue(1d);
    final PutMetricDataRequest request = new PutMetricDataRequest()
        .withNamespace(cloudWatchProperties.getNamespace())
        .withMetricData(datum);

    final Dimension appVersionDimension = new Dimension()
        .withName("AppVersion")
        .withValue(getBuildVersion());

    final Dimension productDimension = new Dimension()
        .withName("Product")
        .withValue(cloudWatchProperties.getMetricProduct());

    final Dimension environmentDimension = new Dimension()
        .withName("Environment")
        .withValue(cloudWatchProperties.getMetricEnvironment());

    final Dimension envIdDimension = new Dimension()
        .withName("Env_id")
        .withValue(cloudWatchProperties.getMetricEnvId());

    final Dimension channelDimension = new Dimension()
        .withName(DIMENSION_NAME_CHANNEL)
        .withValue(CHANNEL_NAME_STRATEGIC);

    datum.withDimensions(appVersionDimension, productDimension, environmentDimension,
        envIdDimension, channelDimension);

    cloudWatchClient.putMetricData(request);
  }

  private String getBuildVersion() {
    if (buildVersion == null) {
      buildVersion = environment.getProperty("app_version");
    }
    return buildVersion;
  }

}
