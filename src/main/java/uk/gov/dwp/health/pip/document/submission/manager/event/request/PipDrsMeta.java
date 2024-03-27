package uk.gov.dwp.health.pip.document.submission.manager.event.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.dwp.health.pip.document.submission.manager.event.Validatable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PipDrsMeta extends Validatable {

  @JsonInclude(NON_NULL)
  @JsonProperty(value = "_comment1")
  private String comment1;

  @JsonInclude(NON_NULL)
  @JsonProperty(value = "_comment2")
  private String comment2;

  @JsonInclude(NON_NULL)
  @JsonProperty(value = "_comment3")
  private String comment3;

  @Size(min = 8, max = 8)
  @JsonProperty(value = "ninoBody")
  private String ninoBody;

  @Size(min = 1, max = 1)
  @JsonProperty(value = "ninoSuffix")
  private String ninoSuffix;

  @NotBlank(message = "Customer reference number, if NINO number not known")
  @JsonProperty(value = "customerReferenceNumber")
  private String customRef;

  @NotBlank(message = "Customer date of birth is required")
  @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Dob must be ISO 8601 format i.e. yyyy-MM-dd")
  @JsonProperty(value = "dateOfBirth")
  private String dob;

  @NotBlank(message = "Customer forename is required")
  @JsonProperty(value = "forename")
  private String forename;

  @NotBlank(message = "Customer surname is required")
  @JsonProperty(value = "surname")
  private String surname;

  @NotBlank(message = "Customer postcode is required")
  @JsonProperty(value = "postcode")
  private String postcode;

  @JsonProperty(value = "staffId")
  @JsonInclude(NON_NULL)
  private String agentStaffId;

  @JsonProperty(value = "documents")
  @NotEmpty(message = "Document must not be empty")
  private List<Document> documentList = new ArrayList<>();
}
