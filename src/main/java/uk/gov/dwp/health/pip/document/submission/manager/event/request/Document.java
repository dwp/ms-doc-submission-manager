package uk.gov.dwp.health.pip.document.submission.manager.event.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.dwp.health.pip.document.submission.manager.event.Validatable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document extends Validatable {

  @JsonProperty(value = "_comment")
  @JsonInclude(NON_NULL)
  @NotBlank(message = "Document description required")
  private String comment;

  @JsonProperty(value = "documentUrl")
  @NotBlank(message = "Document S3 download URL is required")
  private String url;

  @JsonProperty(value = "documentType")
  @NotBlank(message = "Document type required - 1274 for a PIP2, and 1241 for Further evidence")
  private String type;

  @JsonProperty(value = "documentDate")
  @NotBlank(
      message =
          "Document date required - Format is ISO 8601 date and time e.g. 2020-05-12T10:52:54")
  @Pattern(
      regexp = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}",
      message = "Dob must be ISO 8601 format i.e.  2020-05-12T10:52:54")
  private String date;
}
