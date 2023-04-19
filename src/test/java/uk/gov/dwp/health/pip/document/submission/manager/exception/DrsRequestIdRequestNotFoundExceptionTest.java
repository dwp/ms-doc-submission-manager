package uk.gov.dwp.health.pip.document.submission.manager.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DrsRequestIdRequestNotFoundExceptionTest {

  @Test
  @DisplayName("Test create drs request not found exception")
  void testCreateDrsRequestNotFoundException() {
    var cut = new DrsRequestNotFoundException("drs request not found");
    assertThat(cut.getMessage()).isEqualTo("drs request not found");
  }
}
