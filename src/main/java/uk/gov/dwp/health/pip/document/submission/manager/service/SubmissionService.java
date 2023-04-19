package uk.gov.dwp.health.pip.document.submission.manager.service;

@FunctionalInterface
public interface SubmissionService<T, S> {

  S createNewSubmission(T submission);
}
