package uk.gov.dwp.health.pip.document.submission.manager.service;

import uk.gov.dwp.health.pip.document.submission.manager.entity.Documentation;
import uk.gov.dwp.health.pip.document.submission.manager.entity.DrsUpload;
import uk.gov.dwp.health.pip.document.submission.manager.entity.Submission;

import java.util.List;

public interface DataService {

  Submission findSubmissionById(String id);

  Submission findSubmissionByClaimantIdAndClaimId(String claimant, String claim);

  Documentation findDocumentById(String id);

  Documentation createUpdateDocumentation(Documentation documentation);

  Submission createUpdateSubmission(Submission submission);

  Submission findSubmissionByDocumentId(String id);

  DrsUpload createUpdateDrsRequestAudit(DrsUpload requestAudit);

  DrsUpload findDrsRequestByRequestId(String id);

  List<DrsUpload> findRequestsBySubmissionDate(String dayOrWeek);
}
