package uk.gov.dwp.health.pip.document.submission.manager.service;

@FunctionalInterface
public interface SubmissionSupplementaryService<T, S> {

  S attachDocumentToExistingSubmission(T supplementaryDocument);
}
