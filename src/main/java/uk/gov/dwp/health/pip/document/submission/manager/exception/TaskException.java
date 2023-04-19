package uk.gov.dwp.health.pip.document.submission.manager.exception;

import lombok.Getter;

@Getter
public class TaskException extends RuntimeException {
  public TaskException(String msg) {
    super(msg);
  }
}
