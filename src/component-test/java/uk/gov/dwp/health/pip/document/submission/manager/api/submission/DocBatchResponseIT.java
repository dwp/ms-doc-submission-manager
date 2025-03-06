package uk.gov.dwp.health.pip.document.submission.manager.api.submission;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.DimensionFilter;
import com.amazonaws.services.cloudwatch.model.ListMetricsRequest;
import com.amazonaws.services.cloudwatch.model.ListMetricsResult;
import com.amazonaws.services.cloudwatch.model.Metric;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.health.pip.document.submission.manager.api.ApiTest;
import uk.gov.dwp.health.pip.document.submission.manager.dto.requests.submission.SubmissionRequest;
import uk.gov.dwp.health.pip.document.submission.manager.event.response.DrsUploadResponse;
import uk.gov.dwp.health.pip.document.submission.manager.openapi.model.SubmissionResponseObjectV1;
import uk.gov.dwp.health.pip.document.submission.manager.utils.RandomStringUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.dwp.health.pip.document.submission.manager.utils.UrlBuilderUtil.postApplyUrl;

public class DocBatchResponseIT extends ApiTest {

  private static final String HOST_NAME = System.getenv().getOrDefault("AWS_SERVICE_HOSTNAME", "localhost");
  private static final String METRIC_NAME = System.getenv().getOrDefault("AWS_CLOUD_WATCH_SUBMISSION_FAILURE_METRIC_NAME", "submission-failed");
  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static AmazonSQS sqs;
  private static AmazonCloudWatch amazonCloudWatch;

  @BeforeAll
  public static void setupAll() {
    final AwsClientBuilder.EndpointConfiguration endpointConfiguration = getEndpointConfiguration();
    getAmazonCloudWatch(endpointConfiguration);
    getSqsClient(endpointConfiguration);
    objectMapper.disable(DeserializationFeature
        .FAIL_ON_UNKNOWN_PROPERTIES);
  }

  @Test
  public void updateDrsRequestAudit() throws JsonProcessingException {
    final int countBefore = getMetrics().getMetrics().size();
    final DrsUploadResponse drsUploadResponse = new DrsUploadResponse();
    drsUploadResponse.setRequestId(getRequestId());
    drsUploadResponse.setSuccess(false);
    drsUploadResponse.setErrorMessage("Document failed validation at DRS");
    final String messageBody = objectMapper.writeValueAsString(drsUploadResponse);
    final String queueUrl = "http://" + HOST_NAME + ":4566/000000000000/docbatch-batch-response";
    final SendMessageRequest sendMessageRequest = new SendMessageRequest()
        .withMessageBody(messageBody)
        .withQueueUrl(queueUrl);
    LoggerFactory.getLogger(getClass()).info("messageBody = [{}], queueUrl = [{}]", messageBody, queueUrl);
    sqs.sendMessage(sendMessageRequest);
    final long startOfTimer = System.currentTimeMillis();
    int countAfter = countBefore;
    Metric metric = null;
    while (System.currentTimeMillis() - 10000 < startOfTimer && countAfter <= countBefore) {
      final List<Metric> metrics = getMetrics().getMetrics();
      countAfter = metrics.size();
      if (countAfter > 0) {
        metric = metrics.get(metrics.size() - 1);
      }
    }
    assertTrue(countAfter == countBefore + 1, "Expected metrics count to increment by one - went from " + countBefore + " to " + countAfter);
    boolean foundAppVersion = false;
    for (final Dimension dimension : metric.getDimensions()) {
      if (dimension.getName().equals("AppVersion")) {
        assertTrue(dimension.getValue().matches("[0-9]*\\.[0-9]*\\.[0-9]*.*"));
        foundAppVersion = true;
        break;
      }
    }
    assertTrue(foundAppVersion, "Expected AppVersion dimension on metric");
  }

  private String getRequestId() {
    final SubmissionResponseObjectV1 createdDocumentSubmission =
        postRequest(
            postApplyUrl(),
            SubmissionRequest.builder().claimantId(RandomStringUtil.generate(24)).build()
        ).as(SubmissionResponseObjectV1.class);

    return createdDocumentSubmission.getDrsRequestIds().get(0).getRequestId();
  }

  private ListMetricsResult getMetrics() {
    final DimensionFilter dimensionFilter = new DimensionFilter()
        .withName("channel")
        .withValue("strategic");

    ListMetricsRequest request = new ListMetricsRequest()
        .withMetricName(METRIC_NAME)
        .withDimensions(dimensionFilter)
        .withNamespace("test");

    return amazonCloudWatch.listMetrics(request);
  }

  private static void getAmazonCloudWatch(final AwsClientBuilder.EndpointConfiguration endpointConfiguration) {
    amazonCloudWatch = AmazonCloudWatchClientBuilder
        .standard()
        .withEndpointConfiguration(endpointConfiguration)
        .build();
  }

  private static void getSqsClient(final AwsClientBuilder.EndpointConfiguration endpointConfiguration) {
    sqs = AmazonSQSClientBuilder
        .standard()
        .withEndpointConfiguration(endpointConfiguration)
        .build();
  }

  private static AwsClientBuilder.EndpointConfiguration getEndpointConfiguration() {
    final AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
        "http://" + HOST_NAME + ":4566", "eu-west-2"
    );
    return endpointConfiguration;
  }

}
