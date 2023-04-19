package uk.gov.dwp.health.pip.document.submission.manager.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidationExceptionTest {

  @Test
  @DisplayName("Test create validation exception")
  void testCreateValidationException() {
    var cut = new ValidationException("fail validation");
    assertEquals("fail validation", cut.getMessage());
  }
}
