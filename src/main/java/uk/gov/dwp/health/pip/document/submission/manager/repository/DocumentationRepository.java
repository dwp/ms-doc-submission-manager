package uk.gov.dwp.health.pip.document.submission.manager.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.dwp.health.pip.document.submission.manager.entity.Documentation;

@Repository
public interface DocumentationRepository extends CrudRepository<Documentation, String> {}
