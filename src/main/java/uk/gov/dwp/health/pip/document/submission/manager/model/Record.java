package uk.gov.dwp.health.pip.document.submission.manager.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import uk.gov.dwp.health.pip.document.submission.manager.entity.Documentation;
import uk.gov.dwp.health.pip.document.submission.manager.event.request.Document;

import java.util.List;

@Getter
@Setter
@Builder
public class Record {

  private List<Document> drsDocuments;
  private List<Documentation> mongoDocuments;
}
