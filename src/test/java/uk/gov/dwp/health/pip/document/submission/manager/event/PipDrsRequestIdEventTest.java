package uk.gov.dwp.health.pip.document.submission.manager.event;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.util.Collections;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PipDrsRequestIdEventTest {

  private static Stream<Arguments> versionInputs() {
    return Stream.of(
        Arguments.of(null, "0.1"),
        Arguments.of("", "0.1"),
        Arguments.of("  ", "0.1"),
        Arguments.of("0.2", "0.2"));
  }

  @ParameterizedTest
  @MethodSource(value = "versionInputs")
  void testCreatePipDrsEvent(String version, String expected) {
    var now = Instant.now();
    PipDrsEvent cut =
        new PipDrsEvent("drs-event-queue",
            Collections.singletonMap("test", "test"),
            now,
            version);
    assertThat(cut.getOutboundQueue()).isEqualTo("drs-event-queue");
    assertThat(cut.getPayload())
        .usingRecursiveComparison().isEqualTo(Collections.singletonMap("test", "test"));
    assertThat(cut.getMetaData().getTime()).isEqualTo(now.toString());
    assertThat(cut.getVersion()).isEqualTo(expected);
  }
}
