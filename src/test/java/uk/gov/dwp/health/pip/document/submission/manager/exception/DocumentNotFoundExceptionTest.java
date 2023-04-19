package uk.gov.dwp.health.pip.document.submission.manager.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DocumentNotFoundExceptionTest {

  @Test
  void testCreateDocumentNotFoundException() {
    DocumentNotFoundException cut = new DocumentNotFoundException("Document not found");
    assertThat(cut.getMessage()).isEqualTo("Document not found");
  }
}
