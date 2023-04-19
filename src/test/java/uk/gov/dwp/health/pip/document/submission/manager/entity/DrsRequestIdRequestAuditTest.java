package uk.gov.dwp.health.pip.document.submission.manager.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.dwp.health.pip.document.submission.manager.model.DrsStatusEnum;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DrsRequestIdRequestAuditTest {

  @Test
  @DisplayName("Test Create DrsRequestAudit object and assessors")
  void testCreateDrsRequestAuditObjectAndAssessors() {
    LocalDateTime today = LocalDateTime.of(2020, 9, 1, 13, 17, 20);
    LocalDateTime todayMinus1Day = today.minusDays(1);
    DrsUpload actual =
        DrsUpload.builder()
            .submissionId("123")
            .errors("error message")
            .status("SUCCESS")
            .submittedAt(todayMinus1Day)
            .completedAt(today)
            .documentIdIds(List.of(DocumentId.builder().documentId("doc_id_123").build()))
            .build();
    assertThat(actual.getCompletedAt()).isEqualTo(today);
    assertThat(actual.getSubmittedAt()).isEqualTo(todayMinus1Day);
    assertThat(actual.getStatus()).isEqualTo(DrsStatusEnum.SUCCESS.status);
    assertThat(actual.getSubmissionId()).isEqualTo("123");
    assertThat(actual.getErrors()).isEqualTo("error message");
    assertThat(actual.getDocumentIdIds())
        .usingFieldByFieldElementComparator()
        .isEqualTo(List.of(DocumentId.builder().documentId("doc_id_123").build()));
  }
}
