package uk.gov.dwp.health.pip.document.submission.manager.dto.requests.submission;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class ApplicationMeta {

    @Default
    @JsonProperty("start_date")
    private final String startDate = "2020-05-12";

    @Default
    @JsonProperty("completed_date")
    private final String completedDate = "2020-05-20";
}
