package uk.gov.dwp.health.pip.document.submission.manager.exception;

public class SubmissionNotFoundException extends RuntimeException {
  public SubmissionNotFoundException(String msg) {
    super(msg);
  }
}
