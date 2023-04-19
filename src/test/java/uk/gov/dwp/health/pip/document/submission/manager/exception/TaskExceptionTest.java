package uk.gov.dwp.health.pip.document.submission.manager.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskExceptionTest {

  @Test
  void testCreateTaskException() {
    TaskException cut = new TaskException("encryption fail");
    assertThat(cut.getMessage()).isEqualTo("encryption fail");
  }
}
