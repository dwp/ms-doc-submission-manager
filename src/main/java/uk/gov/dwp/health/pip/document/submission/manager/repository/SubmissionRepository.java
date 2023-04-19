package uk.gov.dwp.health.pip.document.submission.manager.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.dwp.health.pip.document.submission.manager.entity.DocumentId;
import uk.gov.dwp.health.pip.document.submission.manager.entity.Submission;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends CrudRepository<Submission, String> {

  Optional<Submission> findByDocumentIdIdsContains(List<DocumentId> documentIdIds);

  Optional<Submission> findByClaimantIdAndApplicationId(String claimantId, String applicationId);
}
