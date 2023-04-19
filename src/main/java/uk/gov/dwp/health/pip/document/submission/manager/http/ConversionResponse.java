package uk.gov.dwp.health.pip.document.submission.manager.http;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConversionResponse {

  @JsonProperty(value = "doc_id")
  private String docId;

  @JsonProperty(value = "status")
  private boolean successful;

  @JsonProperty(value = "mime")
  private String mime;

  @JsonProperty(value = "base64_content")
  private String content;
}
