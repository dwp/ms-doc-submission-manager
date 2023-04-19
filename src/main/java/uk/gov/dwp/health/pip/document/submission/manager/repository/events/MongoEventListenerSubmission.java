package uk.gov.dwp.health.pip.document.submission.manager.repository.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import uk.gov.dwp.health.mongo.changestream.config.properties.WatcherConfigProperties;
import uk.gov.dwp.health.pip.document.submission.manager.entity.Submission;

@Component
@RequiredArgsConstructor
@Slf4j
public class MongoEventListenerSubmission extends AbstractMongoEventListener<Submission> {

  private final WatcherConfigProperties watcherConfigProperties;

  @Override
  public void onBeforeConvert(BeforeConvertEvent<Submission> event) {
    log.info(
            "Set change stream channel.  changeStreamClass is {}, collectionName is submission",
            event.getSource());
    watcherConfigProperties.setChangeStreamChannel(event.getSource(), "submission");
  }
}
