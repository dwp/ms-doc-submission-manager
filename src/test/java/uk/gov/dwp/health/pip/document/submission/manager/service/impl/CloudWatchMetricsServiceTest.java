package uk.gov.dwp.health.pip.document.submission.manager.service.impl;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.StandardUnit;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.core.env.Environment;
import uk.gov.dwp.health.pip.document.submission.manager.config.properties.CloudWatchProperties;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CloudWatchMetricsServiceTest {

  @Test
  public void incrementMetric() {
    final String metricName = "fred";
    final String namespace = "namespace";
    final CloudWatchProperties properties = new CloudWatchProperties();
    properties.setNamespace(namespace);
    final AmazonCloudWatch client = mock(AmazonCloudWatch.class);
    final Environment environment = mock(Environment.class);
    final CloudWatchMetricsServiceImpl service = new CloudWatchMetricsServiceImpl(client, properties, environment);
    service.incrementMetric(metricName);
    final ArgumentCaptor<PutMetricDataRequest> datum = ArgumentCaptor.forClass(PutMetricDataRequest.class);
    verify(client, times(1)).putMetricData(datum.capture());
    final PutMetricDataRequest actualRequest = datum.getValue();
    final List<MetricDatum> metricData = actualRequest.getMetricData();
    assertEquals(1, metricData.size());
    assertEquals(metricName, metricData.get(0).getMetricName());
    assertEquals(StandardUnit.None.toString(), metricData.get(0).getUnit());
    assertEquals(1d, metricData.get(0).getValue());
    assertEquals(5, metricData.get(0).getDimensions().size());
    assertEquals(namespace, actualRequest.getNamespace());
  }
}
