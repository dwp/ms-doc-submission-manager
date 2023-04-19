package uk.gov.dwp.health.pip.document.submission.manager.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DuplicateExceptionTest {

  @Test
  void testCreateDuplicateException() {
    DuplicateException cut = new DuplicateException("submission already exist");
    assertThat(cut.getMessage()).isEqualTo("submission already exist");
  }
}
