package uk.gov.dwp.health.pip.document.submission.manager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.dwp.health.pip.document.submission.manager.config.properties.DrsMetaProperties;
import uk.gov.dwp.health.pip.document.submission.manager.config.properties.EventConfigProperties;
import uk.gov.dwp.health.pip.document.submission.manager.exception.DuplicateException;
import uk.gov.dwp.health.pip.document.submission.manager.openapi.model.AttachDocumentResponseObjectV1;
import uk.gov.dwp.health.pip.document.submission.manager.openapi.model.DrsMetadata;
import uk.gov.dwp.health.pip.document.submission.manager.openapi.model.PipApplicationV1;
import uk.gov.dwp.health.pip.document.submission.manager.openapi.model.RequestId;
import uk.gov.dwp.health.pip.document.submission.manager.openapi.model.ResubmitDrsRequestObjectV1;
import uk.gov.dwp.health.pip.document.submission.manager.openapi.model.ResubmitResponseObject;
import uk.gov.dwp.health.pip.document.submission.manager.openapi.model.SubmissionAttachObjectV1;
import uk.gov.dwp.health.pip.document.submission.manager.openapi.model.SubmissionResponseObjectV1;
import uk.gov.dwp.health.pip.document.submission.manager.service.ResubmissionService;
import uk.gov.dwp.health.pip.document.submission.manager.service.SubmissionService;
import uk.gov.dwp.health.pip.document.submission.manager.service.SubmissionServiceAbstract;
import uk.gov.dwp.health.pip.document.submission.manager.service.SubmissionSupplementaryService;
import uk.gov.dwp.health.pip.document.submission.manager.utils.Batch;
import uk.gov.dwp.health.pip.document.submission.manager.utils.RequestPartition;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
public class V1SubmissionServiceImpl extends SubmissionServiceAbstract
    implements SubmissionService<PipApplicationV1, SubmissionResponseObjectV1>,
        SubmissionSupplementaryService<SubmissionAttachObjectV1, AttachDocumentResponseObjectV1>,
        ResubmissionService<ResubmitDrsRequestObjectV1, ResubmitResponseObject> {

  private final RequestPartition partitionUtil;

  public V1SubmissionServiceImpl(
      EventPublisherImpl publisher,
      S3UrlResolverImpl s3UrlResolver,
      DataServiceImpl dataService,
      DateFormat dateFormat,
      DrsMetaProperties properties,
      EventConfigProperties eventConfigProperties,
      RequestPartition partition) {
    super(publisher, properties, eventConfigProperties, dataService, dateFormat, s3UrlResolver);
    this.partitionUtil = partition;
  }

  @Override
  public SubmissionResponseObjectV1 createNewSubmission(PipApplicationV1 submission) {
    final var applicationId = submission.getApplicationId();
    if (submissionExist(submission.getClaimantId(), applicationId)) {
      final String message =
          String.format(
              "Submission already exist for claimant [%s] and claim [%s]",
              submission.getClaimantId(), applicationId);
      log.info(message);
      throw new DuplicateException(message);
    }
    var subRespObjV2 = new SubmissionResponseObjectV1();
    List<RequestId> requestIds = new ArrayList<>();
    List<Batch> batches = partitionUtil.partition(submission.getDocuments());
    log.info(
        "Initial upload file count [{}] to be partition in [{}]",
        submission.getDocuments().size(),
        batches.size());
    var submissionId = new AtomicReference<String>();
    final DrsMetadata drsMeta = submission.getDrsMetadata();
    IntStream.rangeClosed(0, batches.size() - 1)
        .forEach(
            idx -> {
              if (idx == 0) {
                log.info(
                    "Add [{}] a batch:  FileCount [{}] diskVol [{}] to existing submission",
                    idx,
                    batches.get(idx).getBatch().size(),
                    batches.get(idx).currentVolume());
                var resp =
                    createSubmission(
                        submission.getClaimantId(),
                        submission.getApplicationId(),
                        drsMeta,
                        submission.getApplicationMeta().getStartDate(),
                        submission.getApplicationMeta().getCompletedDate(),
                        submission.getRegion().getValue(),
                        batches.get(idx).getBatch());
                subRespObjV2.setSubmissionId(resp.getSubmissionId());
                requestIds.addAll(resp.getDrsRequestIds());
                submissionId.set(resp.getSubmissionId());
              } else {
                log.info(
                    "Add [{}] batch [{}] to existing submission as further evidence",
                    idx,
                    batches.get(idx).getBatch().size());
                var resp =
                    attachToExisting(
                        Objects.requireNonNull(submissionId).get(),
                        batches.get(idx).getBatch(),
                        drsMeta,
                        submission.getRegion().getValue());
                requestIds.addAll(resp.getDrsRequestIds());
              }
            });
    subRespObjV2.setDrsRequestIds(requestIds);
    return subRespObjV2;
  }

  @Override
  public AttachDocumentResponseObjectV1 attachDocumentToExistingSubmission(
      SubmissionAttachObjectV1 attachObjectV2) {
    final List<Batch> batches = partitionUtil.partition(attachObjectV2.getDocuments());
    log.info(
        "Further evidence upload file count [{}] to be partition in [{}]",
        attachObjectV2.getDocuments().size(),
        batches.size());
    final List<RequestId> collect =
        batches.stream()
            .map(
                batch -> {
                  var resp =
                      attachToExisting(
                          attachObjectV2.getSubmissionId(),
                          batch.getBatch(),
                          attachObjectV2.getDrsMetadata(),
                          attachObjectV2.getRegion().getValue());
                  return resp.getDrsRequestIds().get(0);
                })
            .collect(Collectors.toList());
    var resp = new AttachDocumentResponseObjectV1();
    resp.setDrsRequestIds(collect);
    return resp;
  }

  @Override
  public ResubmitResponseObject resubmit(ResubmitDrsRequestObjectV1 resubmitDrsReqObjV2) {
    return resubmitResponseObject(
        resubmitDrsReqObjV2.getDrsMetadata(),
        resubmitDrsReqObjV2.getDrsRequestIds(),
        resubmitDrsReqObjV2.getRegion().getValue());
  }
}
