package uk.gov.dwp.health.pip.document.submission.manager.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.dwp.health.pip.document.submission.manager.openapi.model.S3RequestDocumentObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BatchPartitionUtilTest {

  private BatchPartitionUtil cut;

  @BeforeEach
  void setup() {
    cut = new BatchPartitionUtil();
  }

  @Test
  @DisplayName("Test throws illegalStateException when docs is null")
  void testThrowsIllegalStateExceptionWhenDocsIsNull() {
    assertThrows(IllegalArgumentException.class, () -> cut.partitionByDiskSize(null, 9000));
  }

  @Test
  @DisplayName("Test empty doc list and empty list returned")
  void testEmptyDocListAndEmptyListReturned() {
    List<S3RequestDocumentObject> emptyDocList = new ArrayList<>();
    var actual = cut.partitionByDiskSize(emptyDocList, 9000);
    assertThat(actual).containsOnly(Collections.emptyList());
  }

  @Test
  @DisplayName("Test throws illegalStateException illegal ceiling size")
  void testThrowsIllegalStateExceptionIllegalCeilingSize() {
    List<S3RequestDocumentObject> docs = new ArrayList<>();
    S3RequestDocumentObject doc = new S3RequestDocumentObject();
    docs.add(doc);
    assertAll(
        "Assert throws ceiling size is less or equals 0kb",
        () -> assertThrows(IllegalArgumentException.class, () -> cut.partitionByDiskSize(docs, 0)),
        () ->
            assertThrows(IllegalArgumentException.class, () -> cut.partitionByDiskSize(docs, -1)));
  }

  @Test
  @DisplayName("Test a collection supplied less than 9mb and original returned")
  void testACollectionSuppliedLessThan9MbAndOriginalReturned() {
    List<S3RequestDocumentObject> docs = new ArrayList<>();
    S3RequestDocumentObject doc = new S3RequestDocumentObject();
    doc.setSize(8999);
    docs.add(doc);
    var actual = cut.partitionByDiskSize(docs, 9000);
    assertThat(actual.size()).isOne();
  }

  @Test
  @DisplayName("Test a collection supplied over 9mb and original partitioned")
  void testACollectionSuppliedOver9MbAndOriginalPartitioned() {
    List<S3RequestDocumentObject> docs = new ArrayList<>();
    var doc1 = new S3RequestDocumentObject();
    doc1.setSize(8999);
    docs.add(doc1);
    var doc2 = new S3RequestDocumentObject();
    doc2.setSize(5);
    docs.add(doc2);
    var doc3 = new S3RequestDocumentObject();
    doc3.setSize(100);
    docs.add(doc3);
    var actual = cut.partitionByDiskSize(docs, 9000);
    assertThat(actual).hasSize(2);
    assertAll(
        "Document partitioned into 2 [8999] and [5, 100]",
        () -> assertTrue(actual.contains(List.of(doc2, doc3))),
        () -> assertTrue(actual.contains(List.of(doc1))));
  }

  @Test
  @DisplayName("Test another collection supplied over 9mb and original partitioned")
  void testAnotherCollectionSuppliedOver9MbAndOriginalPartitioned() {
    List<S3RequestDocumentObject> docs = new ArrayList<>();
    var doc1 = new S3RequestDocumentObject();
    doc1.setSize(8999);
    docs.add(doc1);
    var doc2 = new S3RequestDocumentObject();
    doc2.setSize(5);
    docs.add(doc2);
    var actual = cut.partitionByDiskSize(docs, 9000);
    assertThat(actual).hasSize(2);
    assertAll(
        "Document partitioned into 2 [8999] and [5]",
        () -> assertTrue(actual.contains(List.of(doc2))),
        () -> assertTrue(actual.contains(List.of(doc1))));
  }

  @Test
  @DisplayName("Test front light documents are partition correctly")
  void testFrontLightDocumentsArePartitionCorrectly() {
    List<S3RequestDocumentObject> docs = new LinkedList<>();
    var doc2 = new S3RequestDocumentObject();
    doc2.setSize(5);
    docs.add(doc2);

    var doc3 = new S3RequestDocumentObject();
    doc3.setSize(100);
    docs.add(doc3);

    var doc1 = new S3RequestDocumentObject();
    doc1.setSize(8999);
    docs.add(doc1);

    var actual = cut.partitionByDiskSize(docs, 9000);
    assertThat(actual).hasSize(2);
    assertAll(
        "Document partitioned into 2 [5, 100] and [8999]",
        () -> assertTrue(actual.contains(List.of(doc2, doc3))),
        () -> assertTrue(actual.contains(List.of(doc1))));
  }

  @Test
  @DisplayName("test partition list by chunk")
  void testPartitionListByChunk() {
    List<S3RequestDocumentObject> docs = new LinkedList<>();
    var doc2 = new S3RequestDocumentObject();
    docs.add(doc2);
    var doc3 = new S3RequestDocumentObject();
    docs.add(doc3);
    var doc1 = new S3RequestDocumentObject();
    docs.add(doc1);
    var doc4 = new S3RequestDocumentObject();
    docs.add(doc4);

    var actual = cut.partitionByChunkSize(docs, 2);
    assertThat(actual).hasSize(2);

    actual = cut.partitionByChunkSize(docs, 3);
    assertThat(actual).hasSize(2);

    actual = cut.partitionByChunkSize(docs, 1);
    assertThat(actual).hasSize(4);

    actual = cut.partitionByChunkSize(docs, 4);
    assertThat(actual).hasSize(1);

    actual = cut.partitionByChunkSize(docs, 5);
    assertThat(actual).hasSize(1);
  }

  @Test
  @DisplayName("test partition list by chunk throws illegal arg exception")
  void testPartitionListByChunkThrowsIllegalArgException() {
    final List<S3RequestDocumentObject> docs = new LinkedList<>();
    assertAll(
        "Assert throws ceiling size is less or equals 0kb",
        () -> {
          assertThrows(
              IllegalArgumentException.class,
              () -> {
                var actual = cut.partitionByChunkSize(docs, 0);
              });

          assertThrows(
              IllegalArgumentException.class,
              () -> {
                var actual = cut.partitionByChunkSize(docs, -1);
              });
        });
  }
}
