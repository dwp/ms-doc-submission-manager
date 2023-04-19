package uk.gov.dwp.health.pip.document.submission.manager.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import uk.gov.dwp.health.pip.document.submission.manager.model.DrsStatusEnum;
import uk.gov.dwp.health.pip.document.submission.manager.repository.DocumentationRepository;
import uk.gov.dwp.health.pip.document.submission.manager.repository.DrsRequestAuditRepository;
import uk.gov.dwp.health.pip.document.submission.manager.repository.SubmissionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataServiceImplTest {

  @InjectMocks private DataServiceImpl cut;

  @Mock private DocumentationRepository documentationRepository;
  @Mock private SubmissionRepository submissionRepository;
  @Mock private DrsRequestAuditRepository drsRequestAuditRepository;
  @Captor private ArgumentCaptor<List<DocumentId>> listArgumentCaptor;
  @Captor private ArgumentCaptor<String> stringArgumentCaptor;
  @Captor private ArgumentCaptor<LocalDateTime> dateArgumentCaptor;

  @Nested
  class FindSubmission {
    @Test
    void testFindSubmissionExist() {
      when(submissionRepository.findById(anyString()))
          .thenReturn(Optional.of(Submission.builder().build()));
      Assertions.assertThat(cut.findSubmissionById("1234")).isNotNull();
    }

    @Test
    void testFindSubmissionNotExistNullReturned() {
      when(submissionRepository.findById(anyString())).thenReturn(Optional.empty());
      Assertions.assertThat(cut.findSubmissionById("1234")).isNull();
    }

    @Test
    void testFindSubmissionWithDocumentIdReturnsNull() {
      when(submissionRepository.findByDocumentIdIdsContains(any())).thenReturn(Optional.empty());
      Submission actual = cut.findSubmissionByDocumentId(TestConstant.DOC_ID_2);
      assertThat(actual).isNull();
      verify(submissionRepository).findByDocumentIdIdsContains(listArgumentCaptor.capture());
      assertThat(listArgumentCaptor.getValue())
          .usingElementComparatorOnFields("documentId")
          .contains(DocumentId.builder().documentId(TestConstant.DOC_ID_2).build());
    }

    @Test
    void testFindSubmissionWithDocumentIdReturnSubmission() {
      Submission submission = new Submission();
      submission.setId(TestConstant.SUBMISSION_ID);
      when(submissionRepository.findByDocumentIdIdsContains(any())).thenReturn(Optional.of(submission));
      Submission actual = cut.findSubmissionByDocumentId(TestConstant.DOC_ID_1);
      assertThat(actual.getId()).isEqualTo(TestConstant.SUBMISSION_ID);
      verify(submissionRepository).findByDocumentIdIdsContains(listArgumentCaptor.capture());
      assertThat(listArgumentCaptor.getValue())
          .usingElementComparatorOnFields("documentId")
          .contains(DocumentId.builder().documentId(TestConstant.DOC_ID_1).build());
    }

    @Test
    void testFindSubmissionByClaimantIdAndClaimIdReturnNull() {
      when(submissionRepository.findByClaimantIdAndApplicationId(anyString(), anyString()))
          .thenReturn(Optional.empty());
      Submission actual =
          cut.findSubmissionByClaimantIdAndClaimId(
              TestConstant.CLAIMANT_ID_2, TestConstant.APPLICATION_ID);
      assertThat(actual).isNull();
      verify(submissionRepository)
          .findByClaimantIdAndApplicationId(
              stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
      assertThat(stringArgumentCaptor.getAllValues())
          .containsExactly(TestConstant.CLAIMANT_ID_2, TestConstant.APPLICATION_ID);
    }

    @Test
    void testFindSubmissionByClaimantIdAndClaimId() {
      Submission submission = new Submission();
      submission.setId(TestConstant.SUBMISSION_ID);
      when(submissionRepository.findByClaimantIdAndApplicationId(anyString(), anyString()))
          .thenReturn(Optional.of(submission));
      Submission actual =
          cut.findSubmissionByClaimantIdAndClaimId(
              TestConstant.CLAIMANT_ID_1, TestConstant.APPLICATION_ID);
      assertThat(actual.getId()).isEqualTo(TestConstant.SUBMISSION_ID);
      verify(submissionRepository)
          .findByClaimantIdAndApplicationId(
              stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
      assertThat(stringArgumentCaptor.getAllValues())
          .containsExactly(TestConstant.CLAIMANT_ID_1, TestConstant.APPLICATION_ID);
    }
  }

  @Nested
  class FindDocument {
    @Test
    void testFindDocumentExist() {
      when(documentationRepository.findById(anyString()))
          .thenReturn(Optional.of(Documentation.builder().build()));
      Assertions.assertThat(cut.findDocumentById("1234")).isNotNull();
    }

    @Test
    void testFindDocumentNotExistNullReturned() {
      when(documentationRepository.findById(anyString())).thenReturn(Optional.empty());
      Assertions.assertThat(cut.findDocumentById("1234")).isNull();
    }

    @Test
    @DisplayName("Test find drs request by id and record found")
    void testFindDrsRequestByIdAndRecordFound() {
      when(drsRequestAuditRepository.findById(anyString()))
          .thenReturn(Optional.of(DrsUpload.builder().build()));
      Assertions.assertThat(cut.findDrsRequestByRequestId("1234")).isNotNull();
    }

    @Test
    @DisplayName("Test find drs request by id, null returned")
    void testFindDrsRequestByIdNullReturned() {
      when(drsRequestAuditRepository.findById(anyString())).thenReturn(Optional.empty());
      Assertions.assertThat(cut.findDrsRequestByRequestId("1234")).isNull();
    }
  }

  @Nested
  class CreateNewUpdate {
    @Test
    void testCreateUpdateDocument() {
      Documentation documentation = mock(Documentation.class);
      cut.createUpdateDocumentation(documentation);
      verify(documentationRepository).save(documentation);
    }

    @Test
    void testCreateUpdateSubmission() {
      Submission submission = mock(Submission.class);
      cut.createUpdateSubmission(submission);
      verify(submissionRepository).save(submission);
    }

    @Test
    @DisplayName("Test create drs request audit")
    void testCreateDrsRequestAudit() {
      DrsUpload drsUpload = mock(DrsUpload.class);
      cut.createUpdateDrsRequestAudit(drsUpload);
      verify(drsRequestAuditRepository).save(drsUpload);
    }

    @Test
    void testFindRequestsBySubmissionDateForWeek() {
      LocalDateTime today = LocalDateTime.now();
      LocalDateTime todayMinus7Day = today.minusDays(7);
      DrsUpload drsUpload = new DrsUpload();
      drsUpload.setSubmissionId(TestConstant.SUBMISSION_ID);
      drsUpload.setStatus(DrsStatusEnum.RECEIVED.status);
      when(drsRequestAuditRepository.findBySubmittedAtIsAfter(any()))
          .thenReturn(List.of(drsUpload));
      List<DrsUpload> actual = cut.findRequestsBySubmissionDate("week");
      assertThat(actual.get(0).getSubmissionId()).isEqualTo(TestConstant.SUBMISSION_ID);
      assertThat(actual.get(0).getStatus()).isEqualTo(DrsStatusEnum.RECEIVED.status);
      verify(drsRequestAuditRepository).findBySubmittedAtIsAfter(dateArgumentCaptor.capture());
      assertThat(dateArgumentCaptor.getValue()).isAfterOrEqualTo(todayMinus7Day);
    }

    @Test
    void testFindRequestsBySubmissionDateForDay() {
      LocalDateTime today = LocalDateTime.now();
      LocalDateTime todayMinus1Day = today.minusDays(1);
      DrsUpload drsUpload = new DrsUpload();
      drsUpload.setSubmissionId(TestConstant.SUBMISSION_ID);
      drsUpload.setStatus(DrsStatusEnum.SUCCESS.status);
      when(drsRequestAuditRepository.findBySubmittedAtIsAfter(any()))
          .thenReturn(List.of(drsUpload));
      List<DrsUpload> actual = cut.findRequestsBySubmissionDate("day");
      assertThat(actual.get(0).getSubmissionId()).isEqualTo(TestConstant.SUBMISSION_ID);
      assertThat(actual.get(0).getStatus()).isEqualTo(DrsStatusEnum.SUCCESS.status);
      verify(drsRequestAuditRepository).findBySubmittedAtIsAfter(dateArgumentCaptor.capture());
      assertThat(dateArgumentCaptor.getValue()).isAfterOrEqualTo(todayMinus1Day);
    }

    @Test
    void testFindRequestsBySubmissionDateThrowsIllegalArgument() {
      IllegalArgumentException e =
          assertThrows(
              IllegalArgumentException.class, () -> cut.findRequestsBySubmissionDate("month"));
      assertThat(e.getMessage()).isEqualTo("Illegal method argument passed");
    }
  }
}
