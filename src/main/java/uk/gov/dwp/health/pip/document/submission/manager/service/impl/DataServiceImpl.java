package uk.gov.dwp.health.pip.document.submission.manager.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.dwp.health.pip.document.submission.manager.entity.DocumentId;
import uk.gov.dwp.health.pip.document.submission.manager.entity.Documentation;
import uk.gov.dwp.health.pip.document.submission.manager.entity.DrsUpload;
import uk.gov.dwp.health.pip.document.submission.manager.entity.Submission;
import uk.gov.dwp.health.pip.document.submission.manager.repository.DocumentationRepository;
import uk.gov.dwp.health.pip.document.submission.manager.repository.DrsRequestAuditRepository;
import uk.gov.dwp.health.pip.document.submission.manager.repository.SubmissionRepository;
import uk.gov.dwp.health.pip.document.submission.manager.service.DataService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {

  private final SubmissionRepository submissionRepository;
  private final DocumentationRepository documentationRepository;
  private final DrsRequestAuditRepository drsRequestAuditRepository;

  @Override
  public Submission findSubmissionById(final String id) {
    return submissionRepository.findById(id).orElse(null);
  }

  @Override
  public Submission findSubmissionByClaimantIdAndClaimId(String claimant, String claim) {
    return submissionRepository.findByClaimantIdAndApplicationId(claimant, claim).orElse(null);
  }

  @Override
  public Documentation findDocumentById(final String id) {
    return documentationRepository.findById(id).orElse(null);
  }

  @Override
  public Documentation createUpdateDocumentation(Documentation documentation) {
    return documentationRepository.save(documentation);
  }

  @Override
  public Submission createUpdateSubmission(Submission submission) {
    return submissionRepository.save(submission);
  }

  @Override
  public Submission findSubmissionByDocumentId(String id) {
    return submissionRepository
        .findByDocumentIdIdsContains(List.of(DocumentId.builder().documentId(id).build()))
        .orElse(null);
  }

  @Override
  public DrsUpload createUpdateDrsRequestAudit(DrsUpload requestAudit) {
    return drsRequestAuditRepository.save(requestAudit);
  }

  @Override
  public DrsUpload findDrsRequestByRequestId(String id) {
    return drsRequestAuditRepository.findById(id).orElse(null);
  }

  @Override
  public List<DrsUpload> findRequestsBySubmissionDate(final String dayOrWeek) {
    LocalDateTime today = LocalDateTime.now();
    if ("day".equalsIgnoreCase(dayOrWeek)) {
      return drsRequestAuditRepository.findBySubmittedAtIsAfter(today.minusDays(1));
    } else if ("week".equalsIgnoreCase(dayOrWeek)) {
      return drsRequestAuditRepository.findBySubmittedAtIsAfter(today.minusDays(7));
    } else {
      final String msg = "Illegal method argument passed";
      log.warn(msg);
      throw new IllegalArgumentException(msg);
    }
  }
}
