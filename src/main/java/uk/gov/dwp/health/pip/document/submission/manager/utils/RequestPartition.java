package uk.gov.dwp.health.pip.document.submission.manager.utils;

import lombok.extern.slf4j.Slf4j;
import uk.gov.dwp.health.pip.document.submission.manager.exception.FileExceedLimitException;
import uk.gov.dwp.health.pip.document.submission.manager.openapi.model.S3RequestDocumentObject;

import java.util.LinkedList;
import java.util.List;

@Slf4j
public class RequestPartition implements Partition<List<S3RequestDocumentObject>, List<Batch>> {

  private final int diskVol;
  private final int fileCount;

  public RequestPartition(int vol, int size) {
    this.diskVol = vol;
    this.fileCount = size;
    log.info("Partition diskVol {}, Partition fileCount {}", this.diskVol, this.fileCount);
  }

  @Override
  public List<Batch> partition(List<S3RequestDocumentObject> original) {
    if (!verify(original)) {
      final var message = String.format("A file exceeds allowed batch vol %d", diskVol);
      log.info(message);
      throw new FileExceedLimitException(message);
    }
    LinkedList<Batch> batches = new LinkedList<>();
    original.forEach(
        it -> {
          Batch batch;
          if (batches.isEmpty()) {
            batch = new Batch(this.diskVol, this.fileCount);
            batches.add(batch);
          } else {
            batch = batches.getLast();
          }
          if (!batch.add(it)) {
            batch = new Batch(this.diskVol, this.fileCount);
            batch.add(it);
            batches.add(batch);
          }
        });
    return batches;
  }

  public boolean verify(List<S3RequestDocumentObject> original) {
    return original.stream()
        .noneMatch(
            it -> {
              log.info("Debug: file [{}] file size [{}]", it.getName(), it.getSize());
              return it.getSize() > diskVol;
            });
  }
}
