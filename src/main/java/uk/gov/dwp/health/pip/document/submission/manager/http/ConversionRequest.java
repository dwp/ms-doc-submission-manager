package uk.gov.dwp.health.pip.document.submission.manager.http;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ConversionRequest {

  @JsonProperty(value = "doc_id")
  private String docId;

  @JsonProperty("base64_content")
  private String base64Content;

  @JsonProperty("mime")
  private String mime;
}
