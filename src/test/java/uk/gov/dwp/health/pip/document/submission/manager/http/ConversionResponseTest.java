package uk.gov.dwp.health.pip.document.submission.manager.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConversionResponseTest {

  @Test
  void testCreateConversionResponseAndJsonMappingCorrectly() throws JsonProcessingException {
    ConversionResponse resp =
        ConversionResponse.builder()
            .content("YW55IGNhcm5hbCBwbGVhc3VyZS4=")
            .docId("12345")
            .successful(true)
            .mime("pdf")
            .build();
    ObjectMapper mapper = new ObjectMapper();
    final String actual = mapper.writeValueAsString(resp);
    assertThat(actual)
        .isEqualTo(
            "{\"doc_id\":\"12345\",\"status\":true,\"mime\":\"pdf\",\"base64_content\":\"YW55IGNhcm5hbCBwbGVhc3VyZS4=\"}");
  }
}
