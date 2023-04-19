package uk.gov.dwp.health.pip.document.submission.manager.api.services;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import uk.gov.dwp.health.pip.document.submission.manager.api.ApiTest;
import uk.gov.dwp.health.pip.document.submission.manager.config.MongoClientConnection;
import uk.gov.dwp.health.pip.document.submission.manager.dto.requests.services.ResubmissionRequest;
import uk.gov.dwp.health.pip.document.submission.manager.dto.requests.submission.DocumentSubmissionRequest;
import uk.gov.dwp.health.pip.document.submission.manager.dto.requests.submission.SubmissionRequest;
import uk.gov.dwp.health.pip.document.submission.manager.dto.responses.ErrorResponse;
import uk.gov.dwp.health.pip.document.submission.manager.dto.responses.submission.DocumentSubmissionResponse;
import uk.gov.dwp.health.pip.document.submission.manager.dto.responses.submission.RequestIdResponse;
import uk.gov.dwp.health.pip.document.submission.manager.dto.responses.submission.SubmissionResponse;
import uk.gov.dwp.health.pip.document.submission.manager.utils.RandomStringUtil;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dwp.health.pip.document.submission.manager.utils.UrlBuilderUtil.postAdministrationResubmitUrl;
import static uk.gov.dwp.health.pip.document.submission.manager.utils.UrlBuilderUtil.postApplyUrl;
import static uk.gov.dwp.health.pip.document.submission.manager.utils.UrlBuilderUtil.postAttachUrl;

@Slf4j
public class PostAdministrationResubmitIT extends ApiTest {
  private DocumentSubmissionResponse documentSubmissionResponse;

  @BeforeEach
  public void testSetup() {
    MongoClientConnection.emptyMongoCollections();
    SubmissionRequest submissionRequest =
        SubmissionRequest.builder().claimantId(RandomStringUtil.generate(24)).build();
    SubmissionResponse submissionResponse =
        postRequest(postApplyUrl(), submissionRequest).as(SubmissionResponse.class);

    DocumentSubmissionRequest documentSubmissionRequest =
        DocumentSubmissionRequest.builder()
            .submissionId(submissionResponse.getSubmissionId())
            .build();
    documentSubmissionResponse =
        postRequest(postAttachUrl(), documentSubmissionRequest)
            .as(DocumentSubmissionResponse.class);
  }

  @Test
  public void shouldReturn202StatusCode() {
    ResubmissionRequest resubmissionRequest =
        ResubmissionRequest.builder()
            .drsRequestIdResponses(documentSubmissionResponse.getDrsRequestIdResponses())
            .build();
    Response response = postRequest(postAdministrationResubmitUrl(), resubmissionRequest);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED.value());
  }

  @Test
  public void shouldReturn400StatusCodeForInvalidJson() {
    Response response = postRequest(postAdministrationResubmitUrl(), "}");
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("JSON parse error");
  }

  @Test
  public void shouldReturn404StatusCodeForInvalidDRSRequestId() {
    RequestIdResponse requestIdResponse = new RequestIdResponse();
    requestIdResponse.setRequestId("abc");
    List<RequestIdResponse> requestIdResponses = new ArrayList<>();
    requestIdResponses.add(requestIdResponse);

    ResubmissionRequest resubmissionRequest =
        ResubmissionRequest.builder().drsRequestIdResponses(requestIdResponses).build();
    Response response = postRequest(postAdministrationResubmitUrl(), resubmissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    assertThat(errorResponse.getMessage())
        .contains("Failed DRS request audit does not exist - abc");
  }
}
