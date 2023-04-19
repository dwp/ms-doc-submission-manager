package uk.gov.dwp.health.pip.document.submission.manager.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.dwp.health.pip.document.submission.manager.entity.DrsUpload;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DrsRequestAuditRepository extends CrudRepository<DrsUpload, String> {

  List<DrsUpload> findBySubmittedAtIsAfter(LocalDateTime from);
}
