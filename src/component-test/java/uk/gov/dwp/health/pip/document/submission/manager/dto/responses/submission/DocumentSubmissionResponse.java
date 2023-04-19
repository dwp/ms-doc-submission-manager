package uk.gov.dwp.health.pip.document.submission.manager.dto.responses.submission;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Data
@NoArgsConstructor
public class DocumentSubmissionResponse {
  @JsonProperty("drs_request_ids")
  private List<RequestIdResponse> drsRequestIdResponses;
}
