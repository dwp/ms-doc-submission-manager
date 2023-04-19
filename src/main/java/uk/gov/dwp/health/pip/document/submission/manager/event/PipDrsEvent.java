package uk.gov.dwp.health.pip.document.submission.manager.event;

import uk.gov.dwp.health.integration.message.events.QueueEvent;

import java.time.Instant;
import java.util.Map;

public class PipDrsEvent extends QueueEvent {

  private static final String DEFAULT_VERSION = "0.1";

  public PipDrsEvent(
      String queue,
      Map<String, Object> payload,
      Instant timestamp,
      String version) {
    setOutboundQueue(queue);
    setPayload(payload);
    setMetaData(new TransformationCompletedEventMetaData(timestamp));
    if (version == null || version.isBlank()) {
      setVersion(DEFAULT_VERSION);
    } else {
      setVersion(version);
    }
  }

  public static class TransformationCompletedEventMetaData extends MetaData {
    public TransformationCompletedEventMetaData(Instant timeStamp) {
      super();
      if (timeStamp != null) {
        this.time = timeStamp.toString();
      }
    }
  }
}
