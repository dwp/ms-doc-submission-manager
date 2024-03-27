package uk.gov.dwp.health.pip.document.submission.manager.event.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.dwp.health.pip.document.submission.manager.event.Validatable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DrsUploadRequest extends Validatable {

  @JsonProperty(value = "requestId")
  @NotBlank(message = "Request ID required - A unique identifier from the caller for this request")
  private String requestId;

  @JsonProperty(value = "callerId")
  private String callerId;

  @JsonProperty(value = "correlationId")
  private String correlationId;

  @JsonProperty(value = "responseRoutingKey")
  @NotBlank(message = "Response routing key required")
  private String responseRoutingKey;

  @JsonProperty(value = "envelopes")
  @NotEmpty(message = "At least one DRS meta required")
  private List<PipDrsMeta> metas;

  public String getCallerId() {
    if (this.callerId == null || this.callerId.isBlank()) {
      return "pip-online";
    }
    return callerId;
  }

  public String toString() {
    return String.format("caller ID [%s]", this.callerId);
  }
}
