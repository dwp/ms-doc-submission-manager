package uk.gov.dwp.health.pip.document.submission.manager.api;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import uk.gov.dwp.health.pip.document.submission.manager.exception.CreateSubmissionException;
import uk.gov.dwp.health.pip.document.submission.manager.exception.DrsRequestNotFoundException;
import uk.gov.dwp.health.pip.document.submission.manager.exception.FileExceedLimitException;
import uk.gov.dwp.health.pip.document.submission.manager.openapi.model.ErrorResponseObject;
import uk.org.lidalia.slf4jext.Level;
import uk.org.lidalia.slf4jtest.LoggingEvent;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AppControllerAdviseTest {

  private static AppControllerAdvise cut;
  private TestLogger testLogger = TestLoggerFactory.getTestLogger(AppControllerAdvise.class);

  @BeforeAll
  static void setupSpec() {
    cut = new AppControllerAdvise();
  }

  @BeforeEach
  void setup() {
    testLogger.clearAll();
    ReflectionTestUtils.setField(cut, "log", testLogger);
  }

  @Test
  void testHandleValidationMethodArgHttpMsgExceptionErrorLogged() {
    Exception ex = mock(Exception.class);
    when(ex.getMessage()).thenReturn("bad request");
    ResponseEntity<ErrorResponseObject> actual = cut.handle400(ex);
    assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(Objects.requireNonNull(actual.getBody()).getMessage()).isEqualTo("bad request");
    assertThat(testLogger.getLoggingEvents())
        .containsExactly(new LoggingEvent(Level.ERROR, "Request failed on {}", "bad request"));
  }

  @Test
  void testHandleHttpRequestMethodNotSupportedExceptionErrorLogged() {
    HttpRequestMethodNotSupportedException ex = mock(HttpRequestMethodNotSupportedException.class);
    when(ex.getMessage()).thenReturn("incorrect rest verb");
    ResponseEntity<Void> actual = cut.handle405(ex);
    assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
    assertThat(actual.getBody()).isNull();
    assertThat(testLogger.getLoggingEvents())
        .containsExactly(
            new LoggingEvent(Level.WARN, "Request method fail on {}", "incorrect rest verb"));
  }

  @Test
  void testHandleUnknownRuntimeExceptionErrorLogged() {
    RuntimeException ex = mock(RuntimeException.class);
    when(ex.getMessage()).thenReturn("unknown internal error");
    ResponseEntity<Void> actual = cut.handle500(ex);
    assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(actual.getBody()).isNull();
    assertThat(testLogger.getLoggingEvents())
        .containsExactly(
            new LoggingEvent(Level.ERROR, "Unknown error {}", "unknown internal error"));
  }

  @Test
  void testHandleFailCreateNewSubmission() {
    CreateSubmissionException ex = mock(CreateSubmissionException.class);
    when(ex.getMessage()).thenReturn("fail create new submission");
    ResponseEntity<ErrorResponseObject> actual = cut.handleCreateNewSubmissionException(ex);
    assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(Objects.requireNonNull(actual.getBody()).getMessage())
        .isEqualTo("fail create new submission");
  }

  @Test
  void testHandleDuplicationException() {
    ResponseEntity<Void> actual = cut.handleDuplicationException();
    assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    assertThat(actual.getBody()).isNull();
  }

  @Test
  @DisplayName("Test handle drs request record not found exception")
  void testHandleDrsRecordNotFoundException() {
    RuntimeException exception = new DrsRequestNotFoundException("drs request not found");
    ResponseEntity<ErrorResponseObject> actual = cut.handleRecordNotFound(exception);
    assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(Objects.requireNonNull(actual.getBody()).getMessage())
        .isEqualTo("drs request not found");
  }

  @Test
  @DisplayName("Test handle illegal argument not found exception")
  void testHandleIllegalArgumentNotFoundException() {
    RuntimeException exception = new IllegalArgumentException("Illegal method argument passed");
    ResponseEntity<ErrorResponseObject> actual = cut.handleRecordNotFound(exception);
    assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(Objects.requireNonNull(actual.getBody()).getMessage())
        .isEqualTo("Illegal method argument passed");
  }

  @Test
  @DisplayName("test should return NOT_ACCEPTABLE status")
  void testShouldReturnNotAcceptableStatus() {
    var exception = new FileExceedLimitException("File exceed limit 999");
    ResponseEntity<ErrorResponseObject> actual = cut.handleFileExceedAllowedBatchVol(exception);
    assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
    assertThat(Objects.requireNonNull(actual.getBody()).getMessage())
        .isEqualTo("File exceed limit 999");
  }
}
