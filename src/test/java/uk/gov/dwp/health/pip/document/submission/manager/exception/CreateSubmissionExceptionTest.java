package uk.gov.dwp.health.pip.document.submission.manager.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CreateSubmissionExceptionTest {

  @Test
  void testSubmissionCreateException() {
    var cut = new CreateSubmissionException("Submission fail");
    assertThat(cut.getMessage()).isEqualTo("Submission fail");
  }
}
