package uk.gov.dwp.health.pip.document.submission.manager.event.request;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.stream.Stream;

class PipDrsRequestIdMetaTest {

  private static Stream<Arguments> ninoSuffixSource() {
    return Stream.of(
        Arguments.of("", false, 1), Arguments.of("AB", false, 1), Arguments.of("A", true, 0));
  }

  private static Stream<Arguments> ninoBodySource() {
    return Stream.of(
        Arguments.of("", false, 1),
        Arguments.of("AB12345", false, 1),
        Arguments.of("AB12345", false, 1),
        Arguments.of("AA370773", true, 0));
  }

  private static Stream<Arguments> dateSource() {
    return Stream.of(
        Arguments.of("", false, 2),
        Arguments.of("02-10-1999", false, 1),
        Arguments.of("1-02-1980", false, 1),
        Arguments.of("10-12-1999", false, 1),
        Arguments.of("1999-1-10", false, 1),
        Arguments.of("1999-11-122", false, 1),
        Arguments.of("1999-10-10", true, 0));
  }

  @Test
  void testPipDrsMetaValidatable() {
    PipDrsMeta cut = PipDrsMeta.builder().build();
    Assertions.assertThat(cut.validate()).isFalse();
  }

  @Test
  void testPipDrsMetaValidationOnMissingRequiredProperties() {
    PipDrsMeta cut = PipDrsMeta.builder().build();
    cut.validate();
    Assertions.assertThat(cut.getErrors().size()).isEqualTo(6);
    Assertions.assertThat(cut.errorsToString()).isNotBlank();
  }

  @ParameterizedTest
  @MethodSource("ninoSuffixSource")
  void testPipDrsMetaValidationOnNinoSuffix(String ninoSuffix, boolean isValid, int msgCount) {
    PipDrsMeta cut =
        PipDrsMeta.builder()
            .ninoBody("AA370773")
            .ninoSuffix(ninoSuffix)
            .customRef("CUSTOMER_REF")
            .forename("Micky")
            .surname("Mooose")
            .dob("1999-10-10")
            .postcode("LS12BS")
            .documentList(Collections.singletonList(Document.builder().build()))
            .build();
    Assertions.assertThat(cut.validate()).isEqualTo(isValid);
    Assertions.assertThat(cut.getErrors().size()).isEqualTo(msgCount);
  }

  @ParameterizedTest
  @MethodSource("ninoBodySource")
  void testPipDrsMetaValidationOnNinoBody(String ninoBody, boolean isValid, int msgCount) {
    PipDrsMeta cut =
        PipDrsMeta.builder()
            .ninoBody(ninoBody)
            .ninoSuffix("C")
            .customRef("CUSTOMER_REF")
            .forename("Micky")
            .surname("Mooose")
            .dob("1999-10-10")
            .postcode("LS12BS")
            .documentList(Collections.singletonList(Document.builder().build()))
            .build();
    Assertions.assertThat(cut.validate()).isEqualTo(isValid);
    Assertions.assertThat(cut.getErrors().size()).isEqualTo(msgCount);
  }

  @ParameterizedTest
  @MethodSource("dateSource")
  void testPipDrsMetaValidationOnDob(String date, boolean isValid, int msgCount) {
    PipDrsMeta cut =
        PipDrsMeta.builder()
            .ninoBody("AA370773")
            .ninoSuffix("C")
            .customRef("CUSTOMER_REF")
            .forename("Micky")
            .surname("Mooose")
            .dob(date)
            .postcode("LS12BS")
            .documentList(Collections.singletonList(Document.builder().build()))
            .build();
    Assertions.assertThat(cut.validate()).isEqualTo(isValid);
    Assertions.assertThat(cut.getErrors().size()).isEqualTo(msgCount);
  }
}
