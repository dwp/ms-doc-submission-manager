package uk.gov.dwp.health.pip.document.submission.manager.dto.responses;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@NoArgsConstructor
public class ErrorResponse {
  private String message;
}
