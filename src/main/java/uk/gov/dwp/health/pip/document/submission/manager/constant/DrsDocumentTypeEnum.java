package uk.gov.dwp.health.pip.document.submission.manager.constant;

import java.util.HashMap;
import java.util.Map;

public enum DrsDocumentTypeEnum {
  PIP2_FORM("1274"),
  PIP2_EVIDENCE("1241");

  private static final Map<String, DrsDocumentTypeEnum> map = new HashMap<>();

  static {
    for (DrsDocumentTypeEnum type : DrsDocumentTypeEnum.values()) {
      map.put(type.identifier(), type);
    }
  }

  private final String identifier;

  DrsDocumentTypeEnum(final String identifier) {
    this.identifier = identifier;
  }

  public static DrsDocumentTypeEnum get(final String stage) {
    return map.get(stage);
  }

  public String identifier() {
    return this.identifier;
  }
}
