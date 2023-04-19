package uk.gov.dwp.health.pip.document.submission.manager.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import support.TestConstant;
import uk.gov.dwp.health.pip.document.submission.manager.entity.DocumentId;
import uk.gov.dwp.health.pip.document.submission.manager.entity.Documentation;
import uk.gov.dwp.health.pip.document.submission.manager.entity.DrsUpload;
import uk.gov.dwp.health.pip.document.submission.manager.entity.Submission;
import uk.gov.dwp.health.pip.document.submission.manager.exception.DrsRequestNotFoundException;
import uk.gov.dwp.health.pip.document.submission.manager.model.DrsStatusEnum;
import uk.gov.dwp.health.pip.document.submission.manager.openapi.model.DocumentObject;
import uk.gov.dwp.health.pip.document.submission.manager.openapi.model.QueryRequestResponseObject;
import uk.gov.dwp.health.pip.document.submission.manager.openapi.model.ReportingResponseObject;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QueryServiceImplTest {

  @Captor ArgumentCaptor<String> strArgCaptor;
  @InjectMocks private QueryServiceImpl cut;
  @Mock private DataServiceImpl dataService;

  @Test
  @DisplayName("Test DRS upload request status by ID")
  void testDrsUploadRequestStatusById() {
    String req = "drs-request-id";
    DrsUpload drsAudit =
        DrsUpload.builder()
            .submissionId("submission-id")
            .id("drs-request-id")
            .status("PUBLISHED")
            .build();
    when(dataService.findDrsRequestByRequestId(anyString())).thenReturn(drsAudit);
    QueryRequestResponseObject actual = cut.queryRequestStatusById(req);
    verify(dataService).findDrsRequestByRequestId(strArgCaptor.capture());
    assertThat(strArgCaptor.getValue()).isEqualTo("drs-request-id");
    assertThat(actual.getDrsUploadStatus())
        .isEqualTo(QueryRequestResponseObject.DrsUploadStatusEnum.PUBLISHED);
    assertThat(actual.getRequestId()).isEqualTo("drs-request-id");
    assertThat(actual.getDocuments()).isNull();
  }

  @Test
  @DisplayName("Test DRS requests reporting data")
  void testQueryReportingData() {
    DrsUpload drsRequest1 =
        DrsUpload.builder()
            .id("drs-request-id1")
            .errors("mocked_error_response_json")
            .status(DrsStatusEnum.SUCCESS.status)
            .build();
    DrsUpload drsRequest2 =
        DrsUpload.builder()
            .id("drs-request-id2")
            .status(DrsStatusEnum.SUCCESS.status)
            .build();
    DrsUpload drsRequest3 =
        DrsUpload.builder()
            .id("drs-request-id3")
            .errors("mocked_error_response_json")
            .status(DrsStatusEnum.FAIL.status)
            .build();
    DrsUpload drsRequest4 =
        DrsUpload.builder()
            .id("drs-request-id4")
            .errors("mocked_error_response_json")
            .status(DrsStatusEnum.FAIL.status)
            .build();
    DrsUpload drsRequest5 =
        DrsUpload.builder()
            .id("drs-request-id5")
            .status(DrsStatusEnum.RECEIVED.status)
            .build();
    DrsUpload drsRequest6 =
        DrsUpload.builder()
            .id("drs-request-id6")
            .status(DrsStatusEnum.PUBLISHED.status)
            .build();
    DrsUpload drsRequest7 =
        DrsUpload.builder()
            .id("drs-request-id7")
            .status(DrsStatusEnum.RESUBMITTED.status)
            .build();
    when(dataService.findRequestsBySubmissionDate(anyString()))
        .thenReturn(
            List.of(
                drsRequest1,
                drsRequest2,
                drsRequest3,
                drsRequest4,
                drsRequest5,
                drsRequest6,
                drsRequest7));
    ReportingResponseObject actual = cut.getReportingData("day");
    verify(dataService).findRequestsBySubmissionDate(strArgCaptor.capture());
    assertThat(strArgCaptor.getValue()).isEqualTo("day");
    assertThat(actual.getSubmissionTotal()).isEqualTo(7);
    assertThat(actual.getSuccessfulSubmission()).isEqualTo(2);
    assertThat(actual.getFailedSubmission()).isEqualTo(2);
    assertThat(actual.getInflightSubmission()).isOne();
    assertThat(actual.getReceivedSubmission()).isOne();
    assertThat(actual.getResubmittedSubmission()).isOne();
    assertThat(actual.getFailureDetails().get(0).getSubmissionId())
        .isEqualTo(drsRequest3.getSubmissionId());
    assertThat(actual.getFailureDetails().get(1).getSubmissionId())
        .isEqualTo(drsRequest4.getSubmissionId());
  }

  @Test
  @DisplayName("Test DRS upload request status by ID, document returned")
  void testDrsUploadRequestStatusByIdDocumentReturned() {
    String req = "drs-request-id";
    DrsUpload drsAudit =
        DrsUpload.builder()
            .submissionId("submission-id")
            .id("drs-request-id")
            .status("PUBLISHED")
            .documentIdIds(
                List.of(
                    DocumentId.builder().documentId("1234").build(),
                    DocumentId.builder().documentId("5678").build()))
            .build();
    Documentation documentation1 = new Documentation();
    documentation1.setDocumentType("1724");
    documentation1.setFilename("test1.pdf");
    documentation1.setSizeKb(100);
    documentation1.setId("1234");

    Documentation documentation2 = new Documentation();
    documentation2.setDocumentType("1741");
    documentation2.setFilename("test2.pdf");
    documentation2.setSizeKb(200);
    documentation2.setId("5678");

    when(dataService.findDrsRequestByRequestId(anyString())).thenReturn(drsAudit);
    when(dataService.findDocumentById("1234")).thenReturn(documentation1);
    when(dataService.findDocumentById("5678")).thenReturn(documentation2);

    QueryRequestResponseObject actual = cut.queryRequestStatusById(req);
    verify(dataService).findDrsRequestByRequestId(strArgCaptor.capture());
    assertThat(strArgCaptor.getValue()).isEqualTo("drs-request-id");
    assertThat(actual.getDrsUploadStatus())
        .isEqualTo(QueryRequestResponseObject.DrsUploadStatusEnum.PUBLISHED);
    assertThat(actual.getRequestId()).isEqualTo("drs-request-id");

    DocumentObject expected_doc1 = new DocumentObject();
    expected_doc1.setSubmissionId("submission-id");
    expected_doc1.setDocumentId("1234");
    expected_doc1.setName("test1.pdf");
    expected_doc1.setContentType("1724");
    expected_doc1.setSize(100);
    DocumentObject expected_doc2 = new DocumentObject();
    expected_doc2.setSubmissionId("submission-id");
    expected_doc2.setDocumentId("5678");
    expected_doc2.setName("test2.pdf");
    expected_doc2.setContentType("1741");
    expected_doc2.setSize(200);
    assertThat(actual.getDocuments()).containsSequence(List.of(expected_doc1, expected_doc2));
  }

  @Test
  @DisplayName("Test DRS upload audit not found SubmissionNotFoundException Thrown")
  void testDrsUploadAuditNotFoundSubmissionNotFoundExceptionThrown() {
    String req = "drs-request-id";
    when(dataService.findDrsRequestByRequestId(anyString())).thenReturn(null);
    DrsRequestNotFoundException ex =
        assertThrows(DrsRequestNotFoundException.class, () -> cut.queryRequestStatusById(req));
    assertThat(ex.getMessage()).isEqualTo("DRS request drs-request-id not found");
    verify(dataService).findDrsRequestByRequestId(strArgCaptor.capture());
    assertThat(strArgCaptor.getValue()).isEqualTo("drs-request-id");
  }

  @SuppressWarnings("unused")
  private Documentation documentFixture() {
    return Documentation.builder()
        .id(TestConstant.DOC_ID_2)
        .claimantId(TestConstant.CLAIMANT_ID_1)
        .documentType("application/pdf")
        .filename("test.pdf")
        .sizeKb(1024)
        .timestamp(LocalDateTime.of(2020, 6, 30, 12, 9, 20))
        .build();
  }

  @SuppressWarnings("unused")
  private Submission createSubmissionFixture() {
    return Submission.builder()
        .id(TestConstant.SUBMISSION_ID)
        .claimantId(TestConstant.CLAIMANT_ID_1)
        .documentIdIds(List.of(DocumentId.builder().documentId(TestConstant.DOC_ID_2).build()))
        .build();
  }
}
