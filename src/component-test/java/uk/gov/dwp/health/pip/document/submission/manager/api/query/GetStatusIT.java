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
import uk.gov.dwp.health.pip.document.submission.manager.dto.responses.query.StatusResponse;
import uk.gov.dwp.health.pip.document.submission.manager.dto.responses.submission.SubmissionResponse;
import uk.gov.dwp.health.pip.document.submission.manager.utils.RandomStringUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dwp.health.pip.document.submission.manager.utils.UrlBuilderUtil.getStatusUrl;
import static uk.gov.dwp.health.pip.document.submission.manager.utils.UrlBuilderUtil.postApplyUrl;

@Slf4j
public class GetStatusIT extends ApiTest {
  private String requestId;

  @BeforeEach
  public void testSetup() {
    MongoClientConnection.emptyMongoCollections();
    SubmissionRequest submissionRequest =
        SubmissionRequest.builder().claimantId(RandomStringUtil.generate(24)).build();
    SubmissionResponse submissionResponse =
        postRequest(postApplyUrl(), submissionRequest).as(SubmissionResponse.class);
    requestId = submissionResponse.getDrsRequestIdResponses().get(0).getRequestId();
  }

  @Test
  public void shouldReturn200StatusCodeAndCorrectResponseBody() {
    Response response = getRequest(getStatusUrl(requestId));
    StatusResponse statusResponse = response.as(StatusResponse.class);
    StatusResponse.Documents document = statusResponse.getDocuments().get(0);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
    assertThat(statusResponse.getRequestId()).isEqualTo(requestId);
    assertThat(statusResponse.getDrsUploadStatus()).isEqualTo("PUBLISHED");
    assertThat(document.getSubmissionId().matches("^[a-zA-Z0-9]{24}$"));
    assertThat(document.getDocumentId()).matches("^[a-zA-Z0-9]{24}$");
    assertThat(document.getContentType()).isEqualTo("1274");
    assertThat(document.getName()).isEqualTo("medical-evidence.jpg");
    assertThat(document.getSize()).isEqualTo(5000);
  }

  @Test
  public void shouldReturn400StatusCodeForInvalidIdFormat() {
    Response response = getRequest(getStatusUrl("/"));

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  public void shouldReturn404StatusCodeForIdNotFound() {
    Response response = getRequest(getStatusUrl("abc"));
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    assertThat(errorResponse.getMessage()).isEqualTo("DRS request abc not found");
  }
}
