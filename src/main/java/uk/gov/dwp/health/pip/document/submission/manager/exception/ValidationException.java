package uk.gov.dwp.health.pip.document.submission.manager.exception;

public class ValidationException extends RuntimeException {

  public ValidationException(String msg) {
    super(msg);
  }
}
