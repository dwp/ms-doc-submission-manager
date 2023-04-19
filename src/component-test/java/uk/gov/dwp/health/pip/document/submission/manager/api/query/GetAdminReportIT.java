package uk.gov.dwp.health.pip.document.submission.manager.api.query;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import uk.gov.dwp.health.pip.document.submission.manager.api.ApiTest;
import uk.gov.dwp.health.pip.document.submission.manager.config.MongoClientConnection;
import uk.gov.dwp.health.pip.document.submission.manager.dto.requests.submission.SubmissionRequest;
import uk.gov.dwp.health.pip.document.submission.manager.dto.responses.ErrorResponse;
import uk.gov.dwp.health.pip.document.submission.manager.dto.responses.query.AdminReportResponse;
import uk.gov.dwp.health.pip.document.submission.manager.dto.responses.submission.SubmissionResponse;
import uk.gov.dwp.health.pip.document.submission.manager.utils.RandomStringUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dwp.health.pip.document.submission.manager.utils.UrlBuilderUtil.getReportUrl;
import static uk.gov.dwp.health.pip.document.submission.manager.utils.UrlBuilderUtil.postApplyUrl;

@Slf4j
public class GetAdminReportIT extends ApiTest {

  @BeforeEach
  public void testSetup() {
    MongoClientConnection.emptyMongoCollections();
    SubmissionRequest submissionRequest =
        SubmissionRequest.builder().claimantId(RandomStringUtil.generate(24)).build();
    postRequest(postApplyUrl(), submissionRequest).as(SubmissionResponse.class);
  }

  @Test
  public void shouldReturn200StatusCodeForReportRetrievalLastDay() {
    Response response = getRequest(getReportUrl("day"));
    AdminReportResponse adminReportResponse = response.as(AdminReportResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
    assertThat(adminReportResponse.getSubmissionTotal()).isEqualTo(1);
    assertThat(adminReportResponse.getSuccessfulSubmission()).isEqualTo(0);
    assertThat(adminReportResponse.getFailedSubmission()).isEqualTo(0);
    assertThat(adminReportResponse.getInflightSubmission()).isEqualTo(1);
    assertThat(adminReportResponse.getReceivedSubmission()).isEqualTo(0);
    assertThat(adminReportResponse.getResubmittedSubmission()).isEqualTo(0);
    assertThat(adminReportResponse.getFailureDetails()).isEmpty();
  }

  @Test
  public void shouldReturn200StatusCodeForReportRetrievalLastWeek() {
    Response response = getRequest(getReportUrl("week"));
    AdminReportResponse adminReportResponse = response.as(AdminReportResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
    assertThat(adminReportResponse.getSubmissionTotal()).isEqualTo(1);
    assertThat(adminReportResponse.getSuccessfulSubmission()).isEqualTo(0);
    assertThat(adminReportResponse.getFailedSubmission()).isEqualTo(0);
    assertThat(adminReportResponse.getInflightSubmission()).isEqualTo(1);
    assertThat(adminReportResponse.getReceivedSubmission()).isEqualTo(0);
    assertThat(adminReportResponse.getResubmittedSubmission()).isEqualTo(0);
    assertThat(adminReportResponse.getFailureDetails()).isEmpty();
  }

  @Test
  public void shouldReturn404StatusCodeForInvalidValue() {
    Response response = getRequest(getReportUrl("abc"));
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    assertThat(errorResponse.getMessage()).isEqualTo("Illegal method argument passed");
  }
}
