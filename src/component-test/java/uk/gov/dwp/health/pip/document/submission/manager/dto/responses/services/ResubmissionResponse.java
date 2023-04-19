package uk.gov.dwp.health.pip.document.submission.manager.dto.responses.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Data
@NoArgsConstructor
public class ResubmissionResponse {
  private List<Resubmits> resubmits;

  @Getter
  @Data
  @NoArgsConstructor
  public static class Resubmits {
    @JsonProperty("failed_drs_request_id")
    private String failedDrsRequestId;

    @JsonProperty("retry_drs_request_id")
    private String retryDrsRequestId;
  }
}
