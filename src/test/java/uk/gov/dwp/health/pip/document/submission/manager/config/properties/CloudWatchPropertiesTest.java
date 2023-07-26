package uk.gov.dwp.health.pip.document.submission.manager.config.properties;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CloudWatchPropertiesTest {

  @Test
  public void modifierTest() {
    final CloudWatchProperties cloudWatchProperties = new CloudWatchProperties();
    final String region = "region";
    final String namespace = "namespace";
    final String endpoint = "endpoint";
    final String envId = "envId";
    final String product = "product";
    final String environment = "environment";
    final String accessKey = "accessKey";
    final String secretKey = "secretKey";

    cloudWatchProperties.setAwsAccessKey(accessKey);
    cloudWatchProperties.setAwsSecretAccessKey(secretKey);
    cloudWatchProperties.setAwsRegion(region);
    cloudWatchProperties.setNamespace(namespace);
    cloudWatchProperties.setEndpointOverride(endpoint);
    cloudWatchProperties.setMetricEnvId(envId);
    cloudWatchProperties.setMetricProduct(product);
    cloudWatchProperties.setMetricEnvironment(environment);

    assertEquals(accessKey, cloudWatchProperties.getAwsAccessKey());
    assertEquals(secretKey, cloudWatchProperties.getAwsSecretAccessKey());
    assertEquals(region, cloudWatchProperties.getAwsRegion());
    assertEquals(namespace, cloudWatchProperties.getNamespace());
    assertEquals(endpoint, cloudWatchProperties.getEndpointOverride());
    assertEquals(envId, cloudWatchProperties.getMetricEnvId());
    assertEquals(product, cloudWatchProperties.getMetricProduct());
    assertEquals(environment, cloudWatchProperties.getMetricEnvironment());
  }
}
