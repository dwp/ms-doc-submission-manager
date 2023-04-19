package uk.gov.dwp.health.pip.document.submission.manager.utils;

import lombok.Getter;
import uk.gov.dwp.health.pip.document.submission.manager.openapi.model.S3RequestDocumentObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Batch {

  private final int diskVol;
  private final int fileCount;
  @Getter private final List<S3RequestDocumentObject> batch;
  private final AtomicInteger sum = new AtomicInteger();

  public Batch(int volume, int fileCount) {
    this.diskVol = volume;
    this.fileCount = fileCount;
    this.batch = new ArrayList<>();
  }

  public boolean add(S3RequestDocumentObject one) {
    if (diskVol == -1 && fileCount == -1) {
      this.batch.add(one);
      return true;
    }
    if (diskVol == -1 && fileCount > 0 && isUnderAllowedFileCount()) {
      this.batch.add(one);
      return true;
    }
    if (diskVol > 0 && fileCount == -1 && isUnderAllowedVol(one)) {
      sum.addAndGet(one.getSize());
      this.batch.add(one);
      return true;
    }
    if (isUnderAllowedFileCount() && isUnderAllowedVol(one)) {
      sum.addAndGet(one.getSize());
      this.batch.add(one);
      return true;
    }
    return false;
  }

  private boolean isUnderAllowedFileCount() {
    return batch.size() < fileCount;
  }

  private boolean isUnderAllowedVol(S3RequestDocumentObject one) {
    return currentVolume() + one.getSize() <= diskVol;
  }

  public int currentVolume() {
    return this.sum.get();
  }
}
