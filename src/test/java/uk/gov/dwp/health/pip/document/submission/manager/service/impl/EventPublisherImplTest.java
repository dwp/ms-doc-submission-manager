package uk.gov.dwp.health.pip.document.submission.manager.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.dwp.health.integration.message.events.QueueEvent;
import uk.gov.dwp.health.integration.message.events.QueueEventManager;
import uk.gov.dwp.health.pip.document.submission.manager.config.properties.EventConfigProperties;
import uk.gov.dwp.health.pip.document.submission.manager.event.PipDrsEvent;
import uk.gov.dwp.health.pip.document.submission.manager.event.request.DrsUploadRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventPublisherImplTest {

  @InjectMocks private EventPublisherImpl cut;
  @Mock private QueueEventManager mockedQueueEventManager;
  @Mock private EventConfigProperties props;
  @Mock private ObjectMapper objectMapper;
  @Captor private ArgumentCaptor<QueueEvent> captor;

  @BeforeEach
  void setup() {
    when(props.getVersion()).thenReturn("0.1");
  }

  @Test
  void testEventManagerInvokedOnce() {
    DrsUploadRequest request = mock(DrsUploadRequest.class);
    cut.publishEvent(request);
    verify(mockedQueueEventManager).send(any());
  }

  @Test
  void testEventManagerInvokedWithPayload() {
    DrsUploadRequest request = mock(DrsUploadRequest.class);
    cut.publishEvent(request);
    verify(mockedQueueEventManager).send(captor.capture());
    assertThat(captor.getValue()).isInstanceOf(PipDrsEvent.class);
    verify(objectMapper).convertValue(any(DrsUploadRequest.class), any(TypeReference.class));
  }
}
