package uk.gov.dwp.health.pip.document.submission.manager.dto.requests.submission;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class Document {

    @Default private final String bucket = "pip_bucket";

    @Default
    @JsonProperty("s3Ref")
    private final String s3Ref = "123_TEST.jpg.2020.08.06";

    @Default
    @JsonProperty("content_type")
    private final String contentType = "application/pdf";

    @Default
    @JsonProperty("name")
    private final String name = "medical-evidence.jpg";

    @Default
    @JsonProperty("size")
    private final int size = 5000;

    @Default
    @JsonProperty("dateTime")
    private String dateTime = "2020-09-08T14:30";

    @Default
    @JsonProperty("drsDocType")
    private String drsDocType = "1274";
}
