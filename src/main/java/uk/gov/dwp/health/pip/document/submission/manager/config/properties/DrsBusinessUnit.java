package uk.gov.dwp.health.pip.document.submission.manager.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

@Validated
@Getter
@Setter
public class DrsBusinessUnit {

  @NotBlank(message = "DRS caller ID is required to communicate with  ms-document service")
  private String callerId;

  private String correlationId;
}
