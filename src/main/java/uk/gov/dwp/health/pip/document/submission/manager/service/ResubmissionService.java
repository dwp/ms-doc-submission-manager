package uk.gov.dwp.health.pip.document.submission.manager.service;

@FunctionalInterface
public interface ResubmissionService<T, S> {
  S resubmit(T t);
}
