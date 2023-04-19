package uk.gov.dwp.health.pip.document.submission.manager.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Storage {

  private String type;
  private String uniqueId;
  private String url;
}
