package uk.gov.dwp.health.pip.document.submission.manager.api.submission;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import uk.gov.dwp.health.pip.document.submission.manager.api.ApiTest;
import uk.gov.dwp.health.pip.document.submission.manager.config.MongoClientConnection;
import uk.gov.dwp.health.pip.document.submission.manager.dto.requests.submission.ApplicationMeta;
import uk.gov.dwp.health.pip.document.submission.manager.dto.requests.submission.Document;
import uk.gov.dwp.health.pip.document.submission.manager.dto.requests.submission.DrsMetadata;
import uk.gov.dwp.health.pip.document.submission.manager.dto.requests.submission.SubmissionRequest;
import uk.gov.dwp.health.pip.document.submission.manager.dto.responses.ErrorResponse;
import uk.gov.dwp.health.pip.document.submission.manager.dto.responses.submission.SubmissionResponse;
import uk.gov.dwp.health.pip.document.submission.manager.utils.RandomStringUtil;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dwp.health.pip.document.submission.manager.utils.UrlBuilderUtil.postApplyUrl;

public class PostApplyIT extends ApiTest {
  private SubmissionRequest submissionRequest;

  @BeforeEach
  public void testSetup() {
    MongoClientConnection.emptyMongoCollections();
    submissionRequest =
        SubmissionRequest.builder().claimantId(RandomStringUtil.generate(24)).build();
  }

  @Test
  public void shouldReturn202StatusCode() {
    Response response = postRequest(postApplyUrl(), submissionRequest);
    SubmissionResponse submissionResponse = response.as(SubmissionResponse.class);
    String requestId = submissionResponse.getDrsRequestIdResponses().get(0).getRequestId();

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED.value());
    assertThat(submissionResponse.getSubmissionId()).matches("^[a-zA-Z0-9]{24}$");
    assertThat(requestId).matches("^[a-zA-Z0-9]{24}$");
  }

  @Test
  public void shouldReturn409StatusCodeForExistingSubmission() {
    postRequest(postApplyUrl(), submissionRequest);

    Response response = postRequest(postApplyUrl(), submissionRequest);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT.value());
  }

  @Test
  public void shouldReturn400StatusCodeForInvalidJson() {
    Response response = postRequest(postApplyUrl(), "}");
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("JSON parse error");
  }

  @Test
  public void shouldReturn400StatusCodeIfNoClaimantId() {
    SubmissionRequest submissionRequest = SubmissionRequest.builder().claimantId("").build();
    Response response = postRequest(postApplyUrl(), submissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfNoApplicationId() {
    SubmissionRequest submissionRequest = SubmissionRequest.builder().applicationId("").build();
    Response response = postRequest(postApplyUrl(), submissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfNoRegion() {
    SubmissionRequest submissionRequest = SubmissionRequest.builder().region("").build();
    Response response = postRequest(postApplyUrl(), submissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("JSON parse error");
  }

  @Test
  public void shouldReturn400StatusCodeIfInvalidRegion() {
    SubmissionRequest submissionRequest = SubmissionRequest.builder().region("AA").build();
    Response response = postRequest(postApplyUrl(), submissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("JSON parse error");
  }

  @Test
  public void shouldReturn400StatusCodeIfNoSurname() {
    SubmissionRequest submissionRequest =
        SubmissionRequest.builder().drsMetadata(DrsMetadata.builder().surname("").build()).build();
    Response response = postRequest(postApplyUrl(), submissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfInvalidSurname() {
    SubmissionRequest submissionRequest =
        SubmissionRequest.builder()
            .drsMetadata(DrsMetadata.builder().surname("!@£$%^&@%").build())
            .build();
    Response response = postRequest(postApplyUrl(), submissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfNoForename() {
    SubmissionRequest submissionRequest =
        SubmissionRequest.builder().drsMetadata(DrsMetadata.builder().forename("").build()).build();
    Response response = postRequest(postApplyUrl(), submissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfInvalidForename() {
    SubmissionRequest submissionRequest =
        SubmissionRequest.builder()
            .drsMetadata(DrsMetadata.builder().forename("!@£$%^&@%").build())
            .build();
    Response response = postRequest(postApplyUrl(), submissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfNoDob() {
    SubmissionRequest submissionRequest =
        SubmissionRequest.builder().drsMetadata(DrsMetadata.builder().dob("").build()).build();
    Response response = postRequest(postApplyUrl(), submissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfInvalidDob() {
    SubmissionRequest submissionRequest =
        SubmissionRequest.builder()
            .drsMetadata(DrsMetadata.builder().dob("!@£$%^&@%").build())
            .build();
    Response response = postRequest(postApplyUrl(), submissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("JSON parse error");
  }

  @Test
  public void shouldReturn400StatusCodeIfDobWrongFormat() {
    SubmissionRequest submissionRequest =
        SubmissionRequest.builder()
            .drsMetadata(DrsMetadata.builder().dob("1-1-2000").build())
            .build();
    Response response = postRequest(postApplyUrl(), submissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("JSON parse error");
  }

  @Test
  public void shouldReturn400StatusCodeIfNoNino() {
    SubmissionRequest submissionRequest =
        SubmissionRequest.builder().drsMetadata(DrsMetadata.builder().nino("").build()).build();
    Response response = postRequest(postApplyUrl(), submissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfInvalidNino() {
    SubmissionRequest submissionRequest =
        SubmissionRequest.builder()
            .drsMetadata(DrsMetadata.builder().nino("!@£$%^&@%").build())
            .build();
    Response response = postRequest(postApplyUrl(), submissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfNinoWrongFormat() {
    SubmissionRequest submissionRequest =
        SubmissionRequest.builder()
            .drsMetadata(DrsMetadata.builder().nino("ZZ123456Z").build())
            .build();
    Response response = postRequest(postApplyUrl(), submissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfNoPostCode() {
    SubmissionRequest submissionRequest =
        SubmissionRequest.builder().drsMetadata(DrsMetadata.builder().postcode("").build()).build();
    Response response = postRequest(postApplyUrl(), submissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfNoStartDate() {
    SubmissionRequest submissionRequest =
        SubmissionRequest.builder()
            .applicationMeta(ApplicationMeta.builder().startDate("").build())
            .build();
    Response response = postRequest(postApplyUrl(), submissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfInvalidStartDate() {
    SubmissionRequest submissionRequest =
        SubmissionRequest.builder()
            .applicationMeta(ApplicationMeta.builder().startDate("!@£$|}{>?<M@£$").build())
            .build();
    Response response = postRequest(postApplyUrl(), submissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("JSON parse error");
  }

  @Test
  public void shouldReturn400StatusCodeIfStartDateWrongFormat() {
    SubmissionRequest submissionRequest =
        SubmissionRequest.builder()
            .applicationMeta(ApplicationMeta.builder().startDate("01-01-2000").build())
            .build();
    Response response = postRequest(postApplyUrl(), submissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("JSON parse error");
  }

  @Test
  public void shouldReturn400StatusCodeIfNoCompletedDate() {
    SubmissionRequest submissionRequest =
        SubmissionRequest.builder()
            .applicationMeta(ApplicationMeta.builder().completedDate("").build())
            .build();
    Response response = postRequest(postApplyUrl(), submissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfInvalidCompletedDate() {
    SubmissionRequest submissionRequest =
        SubmissionRequest.builder()
            .applicationMeta(ApplicationMeta.builder().completedDate("!@£$|}{>?<M@£$").build())
            .build();
    Response response = postRequest(postApplyUrl(), submissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("JSON parse error");
  }

  @Test
  public void shouldReturn400StatusCodeICompletedDateWrongFormat() {
    SubmissionRequest submissionRequest =
        SubmissionRequest.builder()
            .applicationMeta(ApplicationMeta.builder().completedDate("01-01-2000").build())
            .build();
    Response response = postRequest(postApplyUrl(), submissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("JSON parse error");
  }

  @Test
  public void shouldReturn400StatusCodeIfNoBucket() {
    SubmissionRequest submissionRequest =
        SubmissionRequest.builder()
            .documents(List.of(Document.builder().bucket("").build()))
            .build();
    Response response = postRequest(postApplyUrl(), submissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfNoS3Ref() {
    SubmissionRequest submissionRequest =
        SubmissionRequest.builder()
            .documents(List.of(Document.builder().s3Ref("").build()))
            .build();
    Response response = postRequest(postApplyUrl(), submissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfNoContentType() {
    SubmissionRequest submissionRequest =
        SubmissionRequest.builder()
            .documents(List.of(Document.builder().contentType("").build()))
            .build();
    Response response = postRequest(postApplyUrl(), submissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfNoName() {
    SubmissionRequest submissionRequest =
        SubmissionRequest.builder().documents(List.of(Document.builder().name("").build())).build();
    Response response = postRequest(postApplyUrl(), submissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfInvalidSize() {
    SubmissionRequest submissionRequest =
        SubmissionRequest.builder().documents(List.of(Document.builder().size(-1).build())).build();
    Response response = postRequest(postApplyUrl(), submissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfNoDateTime() {
    SubmissionRequest submissionRequest =
        SubmissionRequest.builder()
            .documents(List.of(Document.builder().dateTime("").build()))
            .build();
    Response response = postRequest(postApplyUrl(), submissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfInvalidDateTime() {
    SubmissionRequest submissionRequest =
        SubmissionRequest.builder()
            .documents(List.of(Document.builder().dateTime("!@£$)KDFDS").build()))
            .build();
    Response response = postRequest(postApplyUrl(), submissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("JSON parse error");
  }

  @Test
  public void shouldReturn400StatusCodeIfInvalidDateTimeFormat() {
    SubmissionRequest submissionRequest =
        SubmissionRequest.builder()
            .documents(List.of(Document.builder().dateTime("09-09-2008T14:30").build()))
            .build();
    Response response = postRequest(postApplyUrl(), submissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("JSON parse error");
  }

  @Test
  public void shouldReturn400StatusCodeIfNoDrsDocType() {
    SubmissionRequest submissionRequest =
        SubmissionRequest.builder()
            .documents(List.of(Document.builder().drsDocType("").build()))
            .build();
    Response response = postRequest(postApplyUrl(), submissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("JSON parse error");
  }

  @Test
  public void shouldReturn400StatusCodeIfInvalidDrsDocType() {
    SubmissionRequest submissionRequest =
        SubmissionRequest.builder()
            .documents(List.of(Document.builder().drsDocType("!@£@!DFADFDF:{}").build()))
            .build();
    Response response = postRequest(postApplyUrl(), submissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("JSON parse error");
  }
}
