package uk.gov.dwp.health.pip.document.submission.manager.dto.responses.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Data
@NoArgsConstructor
public class StatusResponse {
  @JsonProperty("request_id")
  private String requestId;

  @JsonProperty("drs_upload_status")
  private String drsUploadStatus;

  private List<Documents> documents;

  @Getter
  @Data
  @NoArgsConstructor
  public static class Documents {
    @JsonProperty("submission_id")
    private String submissionId;

    @JsonProperty("document_id")
    private String documentId;

    @JsonProperty("content_type")
    private String contentType;

    private String name;
    private int size;
  }
}
