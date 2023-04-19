package uk.gov.dwp.health.pip.document.submission.manager.event.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DrsUploadResponse {

  @JsonProperty(value = "requestId")
  private String requestId;

  @JsonProperty(value = "_comment")
  private String comment;

  @JsonProperty(value = "success")
  private boolean success;

  @JsonProperty(value = "errorMessage")
  private String errorMessage;

  @JsonProperty(value = "additionalErrorDetails")
  private List<Error> additionalErrorDetails;
}
