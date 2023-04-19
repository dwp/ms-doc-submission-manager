package uk.gov.dwp.health.pip.document.submission.manager.dto.responses.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Data
@NoArgsConstructor
public class AdminReportResponse {
  @JsonProperty("submission_total")
  private int submissionTotal;

  @JsonProperty("successful_submission")
  private int successfulSubmission;

  @JsonProperty("failed_submission")
  private int failedSubmission;

  @JsonProperty("inflight_submission")
  private int inflightSubmission;

  @JsonProperty("received_submission")
  private int receivedSubmission;

  @JsonProperty("resubmitted_submission")
  private int resubmittedSubmission;

  @JsonProperty("failure_details")
  private List<FailureDetails> failureDetails;

  @Getter
  @Data
  @NoArgsConstructor
  public static class FailureDetails {
    @JsonProperty("submission_id")
    private String submissionId;
  }
}
