package uk.gov.dwp.health.pip.document.submission.manager.config.properties;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

class EventConfigPropertiesTest {

  private static Validator VALIDATOR;

  @BeforeAll
  static void setupSpec() {
    VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Test
  void testEventConfig() {
    EventConfigProperties cut = new EventConfigProperties();
    cut.setOutboundBatchUploadQueue("outbound-queue");
    cut.setIncomingRoutingKey("incoming-routing-key");
    cut.setQueueName("incoming-queue");
    cut.setVersion("0.1");
    assertThat(cut.getOutboundBatchUploadQueue()).isEqualTo("outbound-queue");
    assertThat(cut.getIncomingRoutingKey()).isEqualTo("incoming-routing-key");
    assertThat(cut.getQueueName()).isEqualTo("incoming-queue");
    assertThat(cut.getVersion()).isEqualTo("0.1");
    assertThat(VALIDATOR.validate(cut).size()).isZero();
  }

  @ParameterizedTest
  @NullAndEmptySource
  void testEventConfigValidationFail(String key) {
    EventConfigProperties cut = new EventConfigProperties();
    assertThat(VALIDATOR.validate(cut).size()).isEqualTo(3);
  }
}
