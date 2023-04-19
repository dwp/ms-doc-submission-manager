package uk.gov.dwp.health.pip.document.submission.manager.exception;

public class DocumentNotFoundException extends RuntimeException {
  public DocumentNotFoundException(final String msg) {
    super(msg);
  }
}
