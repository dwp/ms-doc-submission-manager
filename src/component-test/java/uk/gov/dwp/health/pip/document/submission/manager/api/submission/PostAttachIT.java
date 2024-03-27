package uk.gov.dwp.health.pip.document.submission.manager.api.submission;

import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import uk.gov.dwp.health.pip.document.submission.manager.api.ApiTest;
import uk.gov.dwp.health.pip.document.submission.manager.config.MongoClientConnection;
import uk.gov.dwp.health.pip.document.submission.manager.dto.requests.submission.Document;
import uk.gov.dwp.health.pip.document.submission.manager.dto.requests.submission.DocumentSubmissionRequest;
import uk.gov.dwp.health.pip.document.submission.manager.dto.requests.submission.DrsMetadata;
import uk.gov.dwp.health.pip.document.submission.manager.dto.requests.submission.SubmissionRequest;
import uk.gov.dwp.health.pip.document.submission.manager.dto.responses.ErrorResponse;
import uk.gov.dwp.health.pip.document.submission.manager.dto.responses.submission.DocumentSubmissionResponse;
import uk.gov.dwp.health.pip.document.submission.manager.dto.responses.submission.SubmissionResponse;
import uk.gov.dwp.health.pip.document.submission.manager.utils.RandomStringUtil;

import java.lang.reflect.Type;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dwp.health.pip.document.submission.manager.utils.UrlBuilderUtil.postApplyUrl;
import static uk.gov.dwp.health.pip.document.submission.manager.utils.UrlBuilderUtil.postAttachUrl;

@Slf4j
public class PostAttachIT extends ApiTest {
  private DocumentSubmissionRequest documentSubmissionRequest;

  @BeforeEach
  public void testSetup() {
    MongoClientConnection.emptyMongoCollections();
    SubmissionRequest submissionRequest =
        SubmissionRequest.builder().claimantId(RandomStringUtil.generate(24)).build();
    SubmissionResponse submissionResponse =
        postRequest(postApplyUrl(), submissionRequest).as(SubmissionResponse.class);
    documentSubmissionRequest =
        DocumentSubmissionRequest.builder()
            .submissionId(submissionResponse.getSubmissionId())
            .build();
  }

  @Test
  public void shouldReturn202StatusCodeAndCorrectResponseBody() {
    Response response = postRequest(postAttachUrl(), documentSubmissionRequest);
    DocumentSubmissionResponse documentSubmissionResponse =
        response.as(DocumentSubmissionResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED.value());
    assertThat(documentSubmissionResponse).isInstanceOf(DocumentSubmissionResponse.class);
  }

  @Test
  public void shouldReturn404StatusCodeForDocumentNotFound() {
    DocumentSubmissionRequest documentSubmissionRequest =
        DocumentSubmissionRequest.builder()
            .submissionId("b0a0d4fb-e6c8-419e-8cb9-af45914bd1a6")
            .build();
    Response response = postRequest(postAttachUrl(), documentSubmissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    assertThat(errorResponse.getMessage())
        .isEqualTo("Submission [b0a0d4fb-e6c8-419e-8cb9-af45914bd1a6] not found");
  }

  @Test
  public void shouldReturn400StatusCodeForInvalidJson() {
    Response response = postRequest(postAttachUrl(), "}");
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("JSON parse error");
  }

  @Test
  public void shouldReturn400StatusCodeIfNoRegion() {
    DocumentSubmissionRequest documentSubmissionRequest =
        DocumentSubmissionRequest.builder().region("").build();
    Response response = postRequest(postAttachUrl(), documentSubmissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("JSON parse error");
  }

  @Test
  public void shouldReturn400StatusCodeIfInvalidRegion() {
    DocumentSubmissionRequest documentSubmissionRequest =
        DocumentSubmissionRequest.builder().region("AA").build();
    Response response = postRequest(postAttachUrl(), documentSubmissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("JSON parse error");
  }

  @Test
  public void shouldReturn400StatusCodeIfNoSurname() {
    DocumentSubmissionRequest documentSubmissionRequest =
        DocumentSubmissionRequest.builder()
            .drsMetadata(DrsMetadata.builder().surname("").build())
            .build();
    Response response = postRequest(postAttachUrl(), documentSubmissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfInvalidSurname() {
    DocumentSubmissionRequest documentSubmissionRequest =
        DocumentSubmissionRequest.builder()
            .drsMetadata(DrsMetadata.builder().surname("!@£$%^&@%").build())
            .build();
    Response response = postRequest(postAttachUrl(), documentSubmissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfNoForename() {
    DocumentSubmissionRequest documentSubmissionRequest =
        DocumentSubmissionRequest.builder()
            .drsMetadata(DrsMetadata.builder().forename("").build())
            .build();
    Response response = postRequest(postAttachUrl(), documentSubmissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfInvalidForename() {
    DocumentSubmissionRequest documentSubmissionRequest =
        DocumentSubmissionRequest.builder()
            .drsMetadata(DrsMetadata.builder().forename("!@£$%^&@%").build())
            .build();
    Response response = postRequest(postAttachUrl(), documentSubmissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfNoDob() {
    DocumentSubmissionRequest documentSubmissionRequest =
        DocumentSubmissionRequest.builder()
            .drsMetadata(DrsMetadata.builder().dob("").build())
            .build();
    Response response = postRequest(postAttachUrl(), documentSubmissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfInvalidDob() {
    DocumentSubmissionRequest documentSubmissionRequest =
        DocumentSubmissionRequest.builder()
            .drsMetadata(DrsMetadata.builder().dob("!@£$%^&@%").build())
            .build();
    Response response = postRequest(postAttachUrl(), documentSubmissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("JSON parse error");
  }

  @Test
  public void shouldReturn400StatusCodeIfDobWrongFormat() {
    DocumentSubmissionRequest documentSubmissionRequest =
        DocumentSubmissionRequest.builder()
            .drsMetadata(DrsMetadata.builder().dob("1-1-2000").build())
            .build();
    Response response = postRequest(postAttachUrl(), documentSubmissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("JSON parse error");
  }

  @Test
  public void shouldReturn400StatusCodeIfNoNino() {
    DocumentSubmissionRequest documentSubmissionRequest =
        DocumentSubmissionRequest.builder()
            .drsMetadata(DrsMetadata.builder().nino("").build())
            .build();
    Response response = postRequest(postAttachUrl(), documentSubmissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfInvalidNino() {
    DocumentSubmissionRequest documentSubmissionRequest =
        DocumentSubmissionRequest.builder()
            .drsMetadata(DrsMetadata.builder().nino("!@£$%^&@%").build())
            .build();
    Response response = postRequest(postAttachUrl(), documentSubmissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfNinoWrongFormat() {
    DocumentSubmissionRequest documentSubmissionRequest =
        DocumentSubmissionRequest.builder()
            .drsMetadata(DrsMetadata.builder().nino("ZZ123456Z").build())
            .build();
    Response response = postRequest(postAttachUrl(), documentSubmissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfNoPostCode() {
    DocumentSubmissionRequest documentSubmissionRequest =
        DocumentSubmissionRequest.builder()
            .drsMetadata(DrsMetadata.builder().postcode("").build())
            .build();
    Response response = postRequest(postAttachUrl(), documentSubmissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfNoBucket() {
    DocumentSubmissionRequest documentSubmissionRequest =
        DocumentSubmissionRequest.builder()
            .documents(List.of(Document.builder().bucket("").build()))
            .build();
    Response response = postRequest(postAttachUrl(), documentSubmissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfNoS3Ref() {
    DocumentSubmissionRequest documentSubmissionRequest =
        DocumentSubmissionRequest.builder()
            .documents(List.of(Document.builder().s3Ref("").build()))
            .build();
    Response response = postRequest(postAttachUrl(), documentSubmissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfNoContentType() {
    DocumentSubmissionRequest documentSubmissionRequest =
        DocumentSubmissionRequest.builder()
            .documents(List.of(Document.builder().contentType("").build()))
            .build();
    Response response = postRequest(postAttachUrl(), documentSubmissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfNoName() {
    DocumentSubmissionRequest documentSubmissionRequest =
        DocumentSubmissionRequest.builder()
            .documents(List.of(Document.builder().name("").build()))
            .build();
    Response response = postRequest(postAttachUrl(), documentSubmissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfInvalidSize() {
    DocumentSubmissionRequest documentSubmissionRequest =
        DocumentSubmissionRequest.builder()
            .documents(List.of(Document.builder().size(-1).build()))
            .build();
    Response response = postRequest(postAttachUrl(), documentSubmissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfNoDateTime() {
    DocumentSubmissionRequest documentSubmissionRequest =
        DocumentSubmissionRequest.builder()
            .documents(List.of(Document.builder().dateTime("").build()))
            .build();
    Response response = postRequest(postAttachUrl(), documentSubmissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("Validation failed");
  }

  @Test
  public void shouldReturn400StatusCodeIfInvalidDateTime() {
    DocumentSubmissionRequest documentSubmissionRequest =
        DocumentSubmissionRequest.builder()
            .documents(List.of(Document.builder().dateTime("!@£$)KDFDS").build()))
            .build();
    Response response = postRequest(postAttachUrl(), documentSubmissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("JSON parse error");
  }

  @Test
  public void shouldReturn400StatusCodeIfInvalidDateTimeFormat() {
    DocumentSubmissionRequest documentSubmissionRequest =
        DocumentSubmissionRequest.builder()
            .documents(List.of(Document.builder().dateTime("09-09-2008T14:30").build()))
            .build();
    Response response = postRequest(postAttachUrl(), documentSubmissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("JSON parse error");
  }

  @Test
  public void shouldReturn400StatusCodeIfNoDrsDocType() {
    DocumentSubmissionRequest documentSubmissionRequest =
        DocumentSubmissionRequest.builder()
            .documents(List.of(Document.builder().drsDocType("").build()))
            .build();
    Response response = postRequest(postAttachUrl(), documentSubmissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("JSON parse error");
  }

  @Test
  public void shouldReturn400StatusCodeIfInvalidDrsDocType() {
    DocumentSubmissionRequest documentSubmissionRequest =
        DocumentSubmissionRequest.builder()
            .documents(List.of(Document.builder().drsDocType("!@£@!DFADFDF:{}").build()))
            .build();
    Response response = postRequest(postAttachUrl(), documentSubmissionRequest);
    ErrorResponse errorResponse = response.as(ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    assertThat(errorResponse.getMessage()).contains("JSON parse error");
  }
}
