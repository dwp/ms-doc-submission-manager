package uk.gov.dwp.health.pip.document.submission.manager.config.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Map;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "drs")
@Validated
@Slf4j
public class DrsMetaProperties {

  @Valid
  @NotEmpty(message = "Business unit details missing e.g. GB, NI")
  private Map<String, DrsBusinessUnit> units;

  public DrsBusinessUnit findBusinessUnitByRegionCode(String regionCode) {
    return units.get(regionCode.toLowerCase());
  }
}
