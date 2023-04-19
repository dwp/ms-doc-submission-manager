package uk.gov.dwp.health.pip.document.submission.manager.dto.responses.submission;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@NoArgsConstructor
public class RequestIdResponse {
    @JsonProperty("request_id")
    private String requestId;
}
