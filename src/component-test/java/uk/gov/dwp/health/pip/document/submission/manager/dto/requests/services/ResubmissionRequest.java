package uk.gov.dwp.health.pip.document.submission.manager.dto.requests.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import uk.gov.dwp.health.pip.document.submission.manager.dto.requests.submission.DrsMetadata;
import uk.gov.dwp.health.pip.document.submission.manager.dto.responses.submission.RequestIdResponse;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder(toBuilder = true)
public class ResubmissionRequest {
    @Default
    @JsonProperty("drs_request_ids")
    private List<RequestIdResponse> drsRequestIdResponses = new ArrayList<>();

    @Default
    @JsonProperty("drs_metadata")
    private final DrsMetadata drsMetadata = DrsMetadata.builder().build();

    @Default
    @JsonProperty("region")
    private final String region = "GB";
}
