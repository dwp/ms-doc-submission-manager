package uk.gov.dwp.health.pip.document.submission.manager.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.dwp.health.pip.document.submission.manager.exception.FileExceedLimitException;
import uk.gov.dwp.health.pip.document.submission.manager.openapi.model.S3RequestDocumentObject;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RequestPartitionTest {

  @Test
  @DisplayName("test partition s3 document into batches")
  void testPartitionS3DocumentIntoBatches() {
    List<S3RequestDocumentObject> docs = new LinkedList<>();
    var doc2 = new S3RequestDocumentObject();
    doc2.setSize(1);
    docs.add(doc2);
    var doc3 = new S3RequestDocumentObject();
    doc3.setSize(2);
    docs.add(doc3);
    var doc1 = new S3RequestDocumentObject();
    doc1.setSize(5);
    docs.add(doc1);
    var doc4 = new S3RequestDocumentObject();
    doc4.setSize(4);
    docs.add(doc4);

    var cut = new RequestPartition(5, 2);
    List<Batch> actual = cut.partition(docs);

    assertThat(actual).hasSize(3);

    assertAll(
        "Assert batch",
        () -> {
          var batch1 = actual.get(0);
          assertThat(batch1.getBatch().get(0).getSize() + batch1.getBatch().get(1).getSize())
              .isEqualTo(3);
          assertThat(batch1.getBatch().size()).isEqualTo(2);
          assertThat(batch1.currentVolume()).isLessThan(5);
          var batch2 = actual.get(1);
          assertThat(batch1.getBatch().get(0).getSize()).isEqualTo(1);
          assertThat(batch2.currentVolume()).isEqualTo(5);
          var batch3 = actual.get(2);
          assertThat(batch1.getBatch().get(0).getSize()).isEqualTo(1);
          assertThat(batch3.currentVolume()).isLessThan(5);
        });
  }

  @Test
  @DisplayName("test should only return one batch with vol -1 and fc -1")
  void testShouldOnlyReturnOneBatchWithVol1AndFc1() {
    List<S3RequestDocumentObject> docs = new LinkedList<>();
    var doc1 = new S3RequestDocumentObject();
    doc1.setSize(99999);
    docs.add(doc1);
    var doc2 = new S3RequestDocumentObject();
    doc2.setSize(1000);
    docs.add(doc2);

    var cut = new RequestPartition(110000, -1);
    assertThat(cut.partition(docs).size()).isEqualTo(1);
  }

  @Test
  @DisplayName("test should return 2 batch with fc -1")
  void testShouldReturn2BatchWithFc1() {
    List<S3RequestDocumentObject> docs = new LinkedList<>();
    var doc1 = new S3RequestDocumentObject();
    doc1.setSize(2);
    docs.add(doc1);
    var doc2 = new S3RequestDocumentObject();
    doc2.setSize(3);
    docs.add(doc2);

    var cut = new RequestPartition(4, -1);
    assertThat(cut.partition(docs).size()).isEqualTo(2);
  }

  @Test
  @DisplayName("test should return 2 batch with vol -1")
  void testShouldReturn2BatchWithVol1() {
    List<S3RequestDocumentObject> docs = new LinkedList<>();
    var doc1 = new S3RequestDocumentObject();
    doc1.setSize(1000);
    docs.add(doc1);
    var doc2 = new S3RequestDocumentObject();
    doc2.setSize(1000);
    docs.add(doc2);

    var cut = new RequestPartition(99999, 1);
    assertThat(cut.partition(docs).size()).isEqualTo(2);
  }

  @Test
  @DisplayName("test should throw file exceed limit exception")
  void testShouldThrowFileExceedLimitException() {
    List<S3RequestDocumentObject> docs = new LinkedList<>();
    var doc1 = new S3RequestDocumentObject();
    doc1.setSize(1000);
    docs.add(doc1);
    var cut = new RequestPartition(999, 2);
    var exception =
        assertThrows(
            FileExceedLimitException.class,
            () -> {
              cut.partition(docs);
            });
    assertThat(exception.getMessage()).isEqualTo("A file exceeds allowed batch vol 999");
  }

  @Test
  @DisplayName("test should not throw file exceed limit exception")
  void testShouldNotThrowFileExceedLimitException() {
    List<S3RequestDocumentObject> docs = new LinkedList<>();
    var doc1 = new S3RequestDocumentObject();
    doc1.setSize(1000);
    docs.add(doc1);
    var doc2 = new S3RequestDocumentObject();
    doc2.setSize(999);
    docs.add(doc2);
    var doc3 = new S3RequestDocumentObject();
    doc3.setSize(998);
    docs.add(doc3);
    var cut = new RequestPartition(1001, 2);
    assertDoesNotThrow(() -> cut.partition(docs));
  }
}
