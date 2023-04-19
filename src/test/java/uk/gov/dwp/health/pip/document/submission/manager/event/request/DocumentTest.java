package uk.gov.dwp.health.pip.document.submission.manager.event.request;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class DocumentTest {

  private static Stream<Arguments> dateSource() {
    return Stream.of(
        Arguments.of("", false, 2),
        Arguments.of("02-10-1999", false, 1),
        Arguments.of("1-02-1980", false, 1),
        Arguments.of("10-12-1999", false, 1),
        Arguments.of("1999-1-10", false, 1),
        Arguments.of("1999-11-122", false, 1),
        Arguments.of("1999-10-10", false, 1),
        Arguments.of("1999-10-10T10:20:22", true, 0));
  }

  @Test
  void testDocumentMetaValidatable() {
    Document cut = Document.builder().build();
    Assertions.assertThat(cut.validate()).isFalse();
  }

  @Test
  void testDocumentFailOnMissingRequiredProperties() {
    Document cut = Document.builder().build();
    cut.validate();
    Assertions.assertThat(cut.getErrors().size()).isEqualTo(4);
    Assertions.assertThat(cut.errorsToString()).isNotBlank();
  }

  @ParameterizedTest
  @MethodSource(value = "dateSource")
  void testDocumentOnDocumentDate(String date, boolean isValid, int msgCount) {
    Document cut =
        Document.builder()
            .comment("comment")
            .type("1241")
            .url("amazon.com/s3/bucket/test")
            .date(date)
            .build();
    Assertions.assertThat(cut.validate()).isEqualTo(isValid);
    Assertions.assertThat(cut.getErrorMessages().size()).isEqualTo(msgCount);
  }
}
