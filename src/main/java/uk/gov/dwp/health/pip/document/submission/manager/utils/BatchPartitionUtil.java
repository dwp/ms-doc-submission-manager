package uk.gov.dwp.health.pip.document.submission.manager.utils;

import lombok.extern.slf4j.Slf4j;
import uk.gov.dwp.health.pip.document.submission.manager.openapi.model.S3RequestDocumentObject;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class BatchPartitionUtil {

  public Collection<List<S3RequestDocumentObject>> partitionByDiskSize(
      List<S3RequestDocumentObject> original, int ceiling) {
    isValid(original, ceiling);
    if (isFitBySize(original, ceiling)) {
      return Collections.singletonList(original);
    }
    final AtomicInteger sum = new AtomicInteger();
    return original.stream()
        .collect(Collectors.groupingBy(it -> sum.addAndGet(it.getSize()) / ceiling))
        .values();
  }

  private void isValid(List<S3RequestDocumentObject> original, int ceiling) {
    if (original == null || ceiling <= 0) {
      final String msg =
          String.format("Input illegal ceiling given %d expect a positive integer", ceiling);
      log.error(msg);
      throw new IllegalArgumentException(msg);
    }
  }

  private boolean isFitBySize(List<S3RequestDocumentObject> files, int limit) {
    final AtomicInteger sum = new AtomicInteger();
    files.forEach(f -> sum.addAndGet(f.getSize()));
    return sum.get() <= limit;
  }

  public Collection<List<S3RequestDocumentObject>> partitionByChunkSize(
      List<S3RequestDocumentObject> original, int ceiling) {
    isValid(original, ceiling);
    if (isFitByChunk(original, ceiling)) {
      return Collections.singletonList(original);
    }
    final AtomicInteger sum = new AtomicInteger();
    return original.stream()
        .collect(Collectors.groupingBy(it -> sum.getAndIncrement() / ceiling))
        .values();
  }

  private boolean isFitByChunk(List<S3RequestDocumentObject> files, int limit) {
    final AtomicInteger sum = new AtomicInteger();
    files.forEach(f -> sum.getAndIncrement());
    return sum.get() <= limit;
  }
}
