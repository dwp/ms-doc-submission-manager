package uk.gov.dwp.health.pip.document.submission.manager.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AttachAdditionalDocumentExceptionTest {

  @Test
  void testCreateAttachAdditionalDocumentException() {
    var cut = new AttachAdditionalDocumentException("fail to attach document");
    assertThat(cut.getMessage()).isEqualTo("fail to attach document");
  }
}
