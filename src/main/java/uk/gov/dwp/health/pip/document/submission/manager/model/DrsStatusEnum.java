package uk.gov.dwp.health.pip.document.submission.manager.model;

public enum DrsStatusEnum {
  RECEIVED("RECEIVED"),
  PUBLISHED("PUBLISHED"),
  SUCCESS("SUCCESS"),
  FAIL("FAIL"),
  RESUBMITTED("RESUBMITTED");

  public final String status;

  DrsStatusEnum(String status) {
    this.status = status;
  }
}
