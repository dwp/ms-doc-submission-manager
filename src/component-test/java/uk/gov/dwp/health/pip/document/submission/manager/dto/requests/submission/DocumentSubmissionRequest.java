package uk.gov.dwp.health.pip.document.submission.manager.dto.requests.submission;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder(toBuilder = true)
public class DocumentSubmissionRequest {

    @Default
    @JsonProperty("submission_id")
    private final String submissionId = "b0a0d4fb-e6c8-419e-8cb9-af45914bd1a6";

    @Default
    @JsonProperty("documents")
    private final List<Document> documents = new ArrayList<>(List.of(Document.builder().build()));;

    @Default
    @JsonProperty("drs_metadata")
    private final DrsMetadata drsMetadata = DrsMetadata.builder().build();

    @Default
    @JsonProperty("region")
    private final String region = "GB";
}
