package uk.gov.dwp.health.pip.document.submission.manager.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.dwp.health.pip.document.submission.manager.exception.TaskException;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class Base64ServiceImplTest {

  @InjectMocks Base64ServiceImpl cut;
  @Mock Base64.Encoder encoder;
  @Mock Base64.Decoder decoder;
  @Captor ArgumentCaptor<String> argumentCaptor;
  @Captor ArgumentCaptor<byte[]> byteCaptor;

  @Test
  void testB64DecodeToByteArray() {
    cut.base64StringToByteArray("test");
    verify(decoder).decode(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isEqualTo("test");
  }

  @Test
  void testHandleBase64DecoderFailureThrowBase64EncodeDecodeException() {
    when(decoder.decode(anyString())).thenThrow(new RuntimeException("decode fail"));
    assertThrows(
        TaskException.class, () -> cut.base64StringToByteArray("illegal_base64_string_payload"));
  }

  @Test
  void testBinaryToBase64Str() {
    final byte[] test = "test".getBytes();
    cut.byteArrayToBase64String(test);
    verify(encoder).encodeToString(byteCaptor.capture());
    assertThat(byteCaptor.getValue()).isEqualTo(test);
  }

  @Test
  void testHandleBinaryEncodeFailureThrowBase64EncodeDecodeException() {
    when(encoder.encodeToString(any())).thenThrow(new RuntimeException("encode fail"));
    final byte[] test = "test".getBytes();
    assertThrows(TaskException.class, () -> cut.byteArrayToBase64String(test));
  }
}
