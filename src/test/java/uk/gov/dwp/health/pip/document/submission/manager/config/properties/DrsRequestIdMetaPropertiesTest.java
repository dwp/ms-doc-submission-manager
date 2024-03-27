package uk.gov.dwp.health.pip.document.submission.manager.config.properties;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DrsRequestIdMetaPropertiesTest {

  private static Validator validator;

  @BeforeAll
  static void setupSpec() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Test
  @DisplayName("test should return business code based on region code")
  void testShouldReturnBusinessCodeBasedOnRegionCode() {
    DrsBusinessUnit gb = new DrsBusinessUnit();
    gb.setCallerId("gb-caller-id");
    gb.setCorrelationId("gb-correlation-id");
    DrsBusinessUnit ni = new DrsBusinessUnit();
    ni.setCallerId("ni-caller-id");
    ni.setCorrelationId("ni-correlation-id");
    DrsMetaProperties cut = new DrsMetaProperties();
    cut.setUnits(Map.of("gb", gb, "ni", ni));
    DrsBusinessUnit actual = cut.findBusinessUnitByRegionCode("gb");
    assertAll(
        "assert business unit details",
        () -> {
          assertEquals("gb-caller-id", actual.getCallerId());
          assertEquals("gb-correlation-id", actual.getCorrelationId());
        });
  }

  @Test
  @DisplayName("test validate fail when business unit detail map is null")
  void testValidateFailWhenBusinessUnitDetailMapIsNull() {
    DrsMetaProperties cut = new DrsMetaProperties();
    assertThat(validator.validate(cut).isEmpty()).isFalse();
  }

  @Test
  @DisplayName("test validate fail when business unit detail map is empty")
  void testValidateFailWhenBusinessUnitDetailMapIsEmpty() {
    DrsMetaProperties cut = new DrsMetaProperties();
    cut.setUnits(Collections.emptyMap());
    assertThat(validator.validate(cut).isEmpty()).isFalse();
  }

  @Test
  @DisplayName("test validate success when business unit detail map has at least one unit")
  void testValidateSuccessWhenBusinessUnitDetailMapHasAtLeastOneUnit() {
    DrsMetaProperties cut = new DrsMetaProperties();
    DrsBusinessUnit gbUnit = new DrsBusinessUnit();
    gbUnit.setCallerId("mock-caller");
    gbUnit.setCorrelationId("mock-correlation");
    cut.setUnits(Collections.singletonMap("gb", gbUnit));
    assertThat(validator.validate(cut).isEmpty()).isTrue();
  }

  @Test
  @DisplayName("test validate fail when business unit detail incorrectly configured")
  void testValidateFailWhenBusinessUnitDetailIncorrectlyConfigured() {
    DrsMetaProperties cut = new DrsMetaProperties();
    DrsBusinessUnit gbUnit = new DrsBusinessUnit();
    gbUnit.setCallerId("");
    gbUnit.setCorrelationId("mock-correlation");
    cut.setUnits(Collections.singletonMap("gb", gbUnit));
    assertThat(validator.validate(cut).isEmpty()).isFalse();
    Set<ConstraintViolation<DrsMetaProperties>> violations = validator.validate(cut);
    ConstraintViolation<DrsMetaProperties> violation = violations.iterator().next();
    assertThat(violation.getMessage())
        .isEqualTo("DRS caller ID is required to communicate with  ms-document service");
  }
}
