package uk.gov.dwp.health.pip.document.submission.manager.dto.requests.submission;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class DrsMetadata {

    @Default
    @JsonProperty("surname")
    private final String surname = "Khanzzz";

    @Default
    @JsonProperty("forename")
    private final String forename = "Hamzazzz";

    @Default
    @JsonProperty("dob")
    private final String dob = "1990-01-20";

    @Default
    @JsonProperty("nino")
    private final String nino = "AA370773A";

    @Default
    @JsonProperty("postcode")
    private final String postcode = "LS2 7UA";


}
