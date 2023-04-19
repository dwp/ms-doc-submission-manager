package uk.gov.dwp.health.pip.document.submission.manager.event.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Error {
  @JsonProperty(value = "_comment")
  private String comment;

  @JsonProperty(value = "error")
  private String reason;

  @JsonProperty(value = "propertyName")
  private String propertyName;

  @JsonProperty(value = "propertyJsonPath")
  private String propertyJsonPath;

  @JsonProperty(value = "propertyValue")
  private String propertyValue;
}
