package uk.gov.dwp.health.pip.document.submission.manager.service;

public interface EncryptionService<T, S> {

  S encrypt(T clear);

  T decrypt(S cipher);
}
