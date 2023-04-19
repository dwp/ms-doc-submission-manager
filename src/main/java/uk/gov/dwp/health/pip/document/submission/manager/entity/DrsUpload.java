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
@Document(collection = "drs_upload")
public class DrsUpload extends MongoChangeStreamIdentifier {

  @Id private String id;
  private String submissionId;
  private List<DocumentId> documentIdIds;
  private String status;
  private String errors;
  private String additionalErrorDetails;
  private LocalDateTime submittedAt;
  private LocalDateTime completedAt;
}
