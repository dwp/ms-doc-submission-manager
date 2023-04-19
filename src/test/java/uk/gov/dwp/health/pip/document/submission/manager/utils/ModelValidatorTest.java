package uk.gov.dwp.health.pip.document.submission.manager.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.dwp.health.pip.document.submission.manager.openapi.model.DrsMetadata;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ModelValidatorTest {

  @Test
  @DisplayName("test should return validation false/failure with message")
  void testShouldReturnValidationFalseFailureWithMessage() {
    var metadata = new DrsMetadata();
    boolean actual = ModelValidator.validate(metadata);
    assertThat(actual).isFalse();
    var actualMessage = ModelValidator.errorsToString();
    assertThat(actualMessage).isNotBlank();
  }

  @Test
  @DisplayName("test should return validation true/success with message")
  void testShouldReturnValidationTrueSuccessWithoutMessage() {
    var metadata = new DrsMetadata();
    metadata.setNino("AA370773A");
    metadata.setSurname("Smithz");
    metadata.setForename("Johnz");
    metadata.setPostcode("LS1 1XX");
    metadata.setDob(LocalDate.now());
    var actual = ModelValidator.validate(metadata);
    assertThat(actual).isTrue();
    var actualMessage = ModelValidator.errorsToString();
    assertThat(actualMessage).isBlank();
  }
}
