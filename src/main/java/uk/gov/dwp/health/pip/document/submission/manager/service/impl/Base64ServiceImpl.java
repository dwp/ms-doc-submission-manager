package uk.gov.dwp.health.pip.document.submission.manager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.dwp.health.pip.document.submission.manager.exception.TaskException;
import uk.gov.dwp.health.pip.document.submission.manager.service.Base64Service;

import java.util.Base64;

@Slf4j
@Service
public class Base64ServiceImpl implements Base64Service {

  private final Base64.Encoder encoder;
  private final Base64.Decoder decoder;

  public Base64ServiceImpl(Base64.Encoder encoder, Base64.Decoder decoder) {
    this.encoder = encoder;
    this.decoder = decoder;
  }

  @Override
  public byte[] base64StringToByteArray(String b64) {
    try {
      return decoder.decode(b64);
    } catch (RuntimeException ex) {
      final String msg = String.format("Fail to decode base 64 string %s", ex.getMessage());
      log.error(msg);
      throw new TaskException(msg);
    }
  }

  @Override
  public String byteArrayToBase64String(byte[] binary) {
    try {
      return encoder.encodeToString(binary);
    } catch (RuntimeException ex) {
      final String msg =
          String.format("Fail to encode binary to base 64 string %s", ex.getMessage());
      log.error(msg);
      throw new TaskException(msg);
    }
  }
}
