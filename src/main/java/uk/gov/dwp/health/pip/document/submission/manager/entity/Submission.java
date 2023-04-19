package uk.gov.dwp.health.pip.document.submission.manager.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import uk.gov.dwp.health.mongo.changestream.extension.MongoChangeStreamIdentifier;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "submission")
@CompoundIndex(
    name = "claimant_id_application_id_idx",
    def = "{ 'claimantId' : 1, 'applicationId' : 1 }",
    unique = true)
public class Submission extends MongoChangeStreamIdentifier {

  @Id private String id;
  private String claimantId;
  private String applicationId;
  private List<DocumentId> documentIdIds;
  private List<DrsRequestId> drs;
  private LocalDate started;
  private LocalDate completed;

}
