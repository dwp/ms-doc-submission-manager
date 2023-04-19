package uk.gov.dwp.health.pip.document.submission.manager.config.properties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith({SpringExtension.class})
@EnableConfigurationProperties(value = {DrsMetaProperties.class})
@TestPropertySource(
    properties = {
      "drs.units.gb.callerId=gb-caller-id",
      "drs.units.gb.correlationId=gb-coll-id",
      "drs.units.ni.callerId=ni-caller-id"
    })
class DrsRequestIdMetaPropertiesLoadingTest {

  @Autowired private DrsMetaProperties cut;

  @Test
  @DisplayName("test should load 2 business units from properties")
  void testShouldLoad2BusinessUnitsFromProperties() {
    assertThat(cut.getUnits().size()).isEqualTo(2);
  }

  @Test
  @DisplayName("test should return gb business unit details")
  void testShouldReturnGbBusinessUnitDetails() {
    DrsBusinessUnit actual = cut.findBusinessUnitByRegionCode("gb");
    assertThat(actual.getCallerId()).isEqualTo("gb-caller-id");
    assertThat(actual.getCorrelationId()).isEqualTo("gb-coll-id");
  }

  @Test
  @DisplayName("test should return ni business unit details")
  void testShouldReturnNiBusinessUnitDetails() {
    DrsBusinessUnit actual = cut.findBusinessUnitByRegionCode("ni");
    assertThat(actual.getCallerId()).isEqualTo("ni-caller-id");
    assertThat(actual.getCorrelationId()).isNull();
  }
}
