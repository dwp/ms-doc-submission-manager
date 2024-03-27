package uk.gov.dwp.health.pip.document.submission.manager.config.properties;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

class CryptoConfigPropertiesTest {

  private static Validator VALIDATOR;
  private CryptoConfigProperties cut;

  @BeforeAll
  static void setupSpec() {
    VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @BeforeEach
  void setup() {
    cut = new CryptoConfigProperties();
  }

  @Test
  void testKmsDataKeyPropFailOnMissingDataKey() {
    assertThat(VALIDATOR.validate(cut).size()).isEqualTo(2);
  }

  @Test
  void testCreateCryptoConfigPropertiesOk() {
    cut.setKmsOverride("mock_endpoint_override");
    cut.setMessageDataKeyId("mock_msg_data_key");
    cut.setKmsKeyCache(true);

    assertThat(VALIDATOR.validate(cut).size()).isZero();
    assertThat(cut.getKmsOverride()).isEqualTo("mock_endpoint_override");
    assertThat(cut.getMessageDataKeyId()).isEqualTo("mock_msg_data_key");
    assertThat(cut.isKmsKeyCache()).isTrue();
  }
}
