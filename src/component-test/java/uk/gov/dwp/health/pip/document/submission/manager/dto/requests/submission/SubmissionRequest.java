package uk.gov.dwp.health.pip.document.submission.manager.dto.requests.submission;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder(toBuilder = true)
public class SubmissionRequest {
    @Default
    @JsonProperty("claimant_id")

    private final String claimantId = "6ab2e541827710233ad6b5c5";

    @Default
    @JsonProperty("application_id")
    private final String applicationId = "6ab2e541827710233ad6b5c5";

    @Default
    @JsonProperty("region")
    private final String region = "GB";

    @Default
    @JsonProperty("drs_metadata")
    private final DrsMetadata drsMetadata = DrsMetadata.builder().build();

    @Default
    @JsonProperty("application_meta")
    private final ApplicationMeta applicationMeta = ApplicationMeta.builder().build();

    @Default
    @JsonProperty("documents")
    private final List<Document> documents = new ArrayList<>(List.of(Document.builder().build()));
}
