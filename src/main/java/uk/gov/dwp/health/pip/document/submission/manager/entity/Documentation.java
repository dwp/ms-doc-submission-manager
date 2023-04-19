package uk.gov.dwp.health.pip.document.submission.manager.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import uk.gov.dwp.health.mongo.changestream.extension.MongoChangeStreamIdentifier;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("document")
public class Documentation extends MongoChangeStreamIdentifier {

  @Id private String id;
  private String claimantId;
  private String filename;
  private String applicationId;
  private int sizeKb;
  private LocalDateTime timestamp;
  private String documentType;
  private List<Storage> storage;
}
