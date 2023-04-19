package uk.gov.dwp.health.pip.document.submission.manager.exception;

public class DrsRequestNotFoundException extends RuntimeException {
  public DrsRequestNotFoundException(String msg) {
    super(msg);
  }
}
