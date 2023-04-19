package uk.gov.dwp.health.pip.document.submission.manager.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConversionRequestTest {

  @Test
  void testCreateConversionRequestWithBuilderAndJsonMappingCorrectly()
      throws JsonProcessingException {
    ConversionRequest request =
        ConversionRequest.builder()
            .docId("1234")
            .base64Content("YW55IGNhcm5hbCBwbGVhc3VyZS4=")
            .mime("jpeg")
            .build();
    assertThat(request).isNotNull();
    var mapper = new ObjectMapper();
    final String actual = mapper.writeValueAsString(request);
    assertThat(actual)
        .isEqualTo(
            "{\"doc_id\":\"1234\",\"base64_content\":\"YW55IGNhcm5hbCBwbGVhc3VyZS4=\",\"mime\":\"jpeg\"}");
  }
}
