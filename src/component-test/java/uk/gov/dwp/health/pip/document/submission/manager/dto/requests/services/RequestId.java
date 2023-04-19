package uk.gov.dwp.health.pip.document.submission.manager.dto.requests.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class RequestId {
    @Default
    @JsonProperty("request_id")
    private final String requestId = "b0a0d4fb-e6c8-419e-8cb9-af45914bd1a6";;
}
