package uk.gov.dwp.health.pip.document.submission.manager.service;

@FunctionalInterface
public interface EventPublisherService<T> {

  void publishEvent(T t);
}
