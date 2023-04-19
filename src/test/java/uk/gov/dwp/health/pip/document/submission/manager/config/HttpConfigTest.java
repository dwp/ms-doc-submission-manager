package uk.gov.dwp.health.pip.document.submission.manager.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import uk.org.lidalia.slf4jtest.LoggingEvent;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HttpConfigTest {

  private final TestLogger testLogger = TestLoggerFactory.getTestLogger(HttpConfig.class);
  private HttpConfig cut;

  @BeforeEach
  void setup() {
    cut = new HttpConfig();
    testLogger.clearAll();
  }

  @Test
  void testCreateCustomRestTemplateCustomErrorHandler406() {
    RestTemplate actual = cut.restTemplateForMultipartFile();
    assertThat(actual).isNotNull();
  }

  @Test
  void testCreateRestTemplateForPostEntity() {
    RestTemplate actual = cut.restTemplateForEntity();
    assertThat(actual).isNotNull();
  }

  @Test
  void testCustomErrorHandlerOverride406Behaviour() throws IOException {
    RestTemplate actual = cut.restTemplateForMultipartFile();
    ResponseErrorHandler handler = actual.getErrorHandler();
    ClientHttpResponse response = mock(ClientHttpResponse.class);
    when(response.getStatusCode()).thenReturn(HttpStatus.NOT_ACCEPTABLE);
    assertThat(handler.hasError(response)).isFalse();
    handler.handleError(response);
  }

  @Test
  void testCustomErrorHandlerDefault500Unchanged() throws IOException {
    RestTemplate actual = cut.restTemplateForMultipartFile();
    ResponseErrorHandler handler = actual.getErrorHandler();
    ClientHttpResponse response = mock(ClientHttpResponse.class);
    when(response.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(handler.hasError(response)).isTrue();
  }

  @Test
  void testCustomErrorHandlerLogsHttpError() throws IOException {
    ReflectionTestUtils.setField(cut, "log", testLogger);
    RestTemplate actual = cut.restTemplateForMultipartFile();
    ResponseErrorHandler handler = actual.getErrorHandler();
    ClientHttpResponse response = mock(ClientHttpResponse.class);
    when(response.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
    when(response.getBody())
        .thenReturn(new ByteArrayInputStream("INTERNAL SERVER ERROR".getBytes()));
    handler.handleError(response);
    List<LoggingEvent> events = testLogger.getLoggingEvents();
    assertThat(events).hasSize(2);
    assertThat(events.get(0).getLevel().name()).isEqualTo(LogLevel.INFO.name());
    assertThat(events.get(0).getMessage()).isEqualTo("Create file-upload rest template bean");
    assertThat(events.get(1).getLevel().name()).isEqualTo(LogLevel.ERROR.name());
    assertThat(events.get(1).getMessage()).isEqualTo("Response error {}, {}");
    assertThat(events.get(1).getArguments()).containsSequence(500, "INTERNAL SERVER ERROR");
  }
}
