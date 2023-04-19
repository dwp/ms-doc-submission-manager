package uk.gov.dwp.health.pip.document.submission.manager.utils;

public interface Partition<T, S> {

  S partition(T original);
}
