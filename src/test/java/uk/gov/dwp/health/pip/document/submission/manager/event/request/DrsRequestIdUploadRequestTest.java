package uk.gov.dwp.health.pip.document.submission.manager.event.request;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;

class DrsRequestIdUploadRequestTest {

  @Test
  void testDrsMetaEventValidatable() {
    DrsUploadRequest cut = DrsUploadRequest.builder().build();
    Assertions.assertThat(cut.validate()).isFalse();
  }

  @Test
  void testDrsMetaEventFailOnMissingRequiredProperties() {
    DrsUploadRequest cut = DrsUploadRequest.builder().build();
    cut.validate();
    Assertions.assertThat(cut.getErrors().size()).isEqualTo(3);
    Assertions.assertThat(cut.errorsToString()).isNotBlank();
  }

  @ParameterizedTest
  @NullAndEmptySource
  void testDrsMetaCallerIdDefaultToPipOnline(String callerId) {
    DrsUploadRequest cut = DrsUploadRequest.builder().callerId(callerId).build();
    assertThat(cut.getCallerId()).isEqualTo("pip-online");
  }

  @Test
  void testDrsMetaCallerId() {
    DrsUploadRequest cut = DrsUploadRequest.builder().callerId("custom-caller-id").build();
    assertThat(cut.getCallerId()).isEqualTo("custom-caller-id");
  }

  @Test
  @DisplayName("test toString returns caller id")
  void testToStringReturnsCallerId() {
    DrsUploadRequest cut = DrsUploadRequest.builder().callerId("custom-caller-id").build();
    assertThat(cut.toString()).isEqualTo("caller ID [custom-caller-id]");
  }
}
