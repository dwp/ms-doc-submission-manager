package uk.gov.dwp.health.pip.document.submission.manager.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.dwp.health.integration.message.events.QueueEventManager;
import uk.gov.dwp.health.pip.document.submission.manager.config.properties.EventConfigProperties;
import uk.gov.dwp.health.pip.document.submission.manager.event.PipDrsEvent;
import uk.gov.dwp.health.pip.document.submission.manager.event.request.DrsUploadRequest;
import uk.gov.dwp.health.pip.document.submission.manager.exception.TaskException;
import uk.gov.dwp.health.pip.document.submission.manager.service.EventPublisherService;

import java.time.Instant;

@Slf4j
@Service
public class EventPublisherImpl implements EventPublisherService<DrsUploadRequest> {

  private final QueueEventManager queueEventManager;
  private final EventConfigProperties configProperties;
  private final ObjectMapper objectMapper;

  @Autowired
  public EventPublisherImpl(
      QueueEventManager queueEventManager, EventConfigProperties props, ObjectMapper objectMapper) {
    this.queueEventManager = queueEventManager;
    this.configProperties = props;
    this.objectMapper = objectMapper;
  }

  @Override
  public void publishEvent(DrsUploadRequest request) {
    try {
      queueEventManager.send(
          new PipDrsEvent(
              configProperties.getOutboundBatchUploadQueue(),
              objectMapper.convertValue(request, new TypeReference<>() {}),
              Instant.now(),
              configProperties.getVersion()));
      log.info("DRS message published and destinate to {}", request.toString());
    } catch (Exception ex) {
      final String msg = String.format("Fail to publish message %s", ex.getMessage());
      log.error(msg);
      throw new TaskException(msg);
    }
  }
}
