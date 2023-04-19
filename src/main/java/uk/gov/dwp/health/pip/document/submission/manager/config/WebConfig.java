package uk.gov.dwp.health.pip.document.submission.manager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uk.gov.dwp.health.logging.IncomingInterceptor;
import uk.gov.dwp.health.logging.LoggerContext;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new IncomingInterceptor(new LoggerContext()));
  }
}
