package uk.gov.dwp.health.pip.document.submission.manager.config.properties;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

class DrsRequestIdBusinessUnitTest {

  private static Validator VALIDATOR;

  @BeforeAll
  static void setupSpec() {
    VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Test
  void testDrsMetaProperties() {
    DrsBusinessUnit cut = new DrsBusinessUnit();
    cut.setCallerId("caller_id_123");
    cut.setCorrelationId("coll_id_123");
    assertThat(cut.getCallerId()).isEqualTo("caller_id_123");
    assertThat(cut.getCorrelationId()).isEqualTo("coll_id_123");
    assertThat(VALIDATOR.validate(cut).size()).isZero();
  }

  @ParameterizedTest
  @NullAndEmptySource
  void testDrsMetaPropertiesValidationFail(String callerId) {
    DrsBusinessUnit cut = new DrsBusinessUnit();
    cut.setCallerId(callerId);
    cut.setCorrelationId("coll_id_123");
    assertThat(VALIDATOR.validate(cut).size()).isEqualTo(1);
  }
}
