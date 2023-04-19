package uk.gov.dwp.health.pip.document.submission.manager.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SubmissionNotFoundExceptionTest {

  @Test
  void testCreateSubmissionNotFoundException() {
    SubmissionNotFoundException cut = new SubmissionNotFoundException("submission not found");
    assertThat(cut.getMessage()).isEqualTo("submission not found");
  }
}
