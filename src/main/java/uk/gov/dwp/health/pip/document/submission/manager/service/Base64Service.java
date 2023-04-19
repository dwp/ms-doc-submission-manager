package uk.gov.dwp.health.pip.document.submission.manager.service;

public interface Base64Service {

  byte[] base64StringToByteArray(final String b64);

  String byteArrayToBase64String(final byte[] binary);
}
