package uk.gov.dwp.health.pip.document.submission.manager.config;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import uk.gov.dwp.health.logging.IncomingInterceptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class WebConfigTest {

  @Test
  void should_add_incoming_interceptor_web_config() {
    var webConfig = new WebConfig();
    var registry = spy(new InterceptorRegistry());
    webConfig.addInterceptors(registry);
    var captor = ArgumentCaptor.forClass(IncomingInterceptor.class);
    verify(registry).addInterceptor(captor.capture());
    assertThat(captor.getValue()).isInstanceOf(IncomingInterceptor.class);
  }
}
