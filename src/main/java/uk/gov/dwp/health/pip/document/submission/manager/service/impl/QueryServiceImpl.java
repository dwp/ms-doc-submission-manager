package uk.gov.dwp.health.pip.document.submission.manager.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.dwp.health.pip.document.submission.manager.entity.DocumentId;
import uk.gov.dwp.health.pip.document.submission.manager.entity.Documentation;
import uk.gov.dwp.health.pip.document.submission.manager.entity.DrsUpload;
import uk.gov.dwp.health.pip.document.submission.manager.exception.DrsRequestNotFoundException;
import uk.gov.dwp.health.pip.document.submission.manager.model.DrsStatusEnum;
import uk.gov.dwp.health.pip.document.submission.manager.openapi.model.DocumentObject;
import uk.gov.dwp.health.pip.document.submission.manager.openapi.model.QueryRequestResponseObject;
import uk.gov.dwp.health.pip.document.submission.manager.openapi.model.ReportingResponseObject;
import uk.gov.dwp.health.pip.document.submission.manager.openapi.model.SubmissionId;
import uk.gov.dwp.health.pip.document.submission.manager.service.DataService;
import uk.gov.dwp.health.pip.document.submission.manager.service.QueryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueryServiceImpl implements QueryService {

  private final DataService dataService;

  @Override
  public QueryRequestResponseObject queryRequestStatusById(String id) {
    var drsRequestAudit = dataService.findDrsRequestByRequestId(id);
    return Optional.ofNullable(drsRequestAudit)
        .map(this::mapSubmissionToQuerySubmissionResponseDto)
        .orElseThrow(
            () -> {
              final String msg = String.format("DRS request %s not found", id);
              log.warn(msg);
              throw new DrsRequestNotFoundException(msg);
            });
  }

  @Override
  public ReportingResponseObject getReportingData(final String dayOrWeek) {
    List<DrsUpload> drsRequests = dataService.findRequestsBySubmissionDate(dayOrWeek);
    int totalSubmissions = drsRequests.size();
    int totalSuccessfulDrsRequests = 0;
    int totalFailedDrsRequests = 0;
    int totalPublishedDrsRequests = 0;
    int totalReceivedDrsRequests = 0;
    int totalResubmittedDrsRequests = 0;
    List<SubmissionId> failureDetails = new ArrayList<>();
    for (DrsUpload drsRequest : drsRequests) {
      if (DrsStatusEnum.SUCCESS.status.equals(drsRequest.getStatus())) {
        totalSuccessfulDrsRequests += 1;
      } else if (DrsStatusEnum.FAIL.status.equals(drsRequest.getStatus())) {
        SubmissionId subId = new SubmissionId();
        subId.setSubmissionId(drsRequest.getSubmissionId());
        failureDetails.add(subId);
        totalFailedDrsRequests += 1;
      } else if (DrsStatusEnum.RECEIVED.status.equals(drsRequest.getStatus())) {
        totalReceivedDrsRequests += 1;
      } else if (DrsStatusEnum.PUBLISHED.status.equals(drsRequest.getStatus())) {
        totalPublishedDrsRequests += 1;
      } else if (DrsStatusEnum.RESUBMITTED.status.equals(drsRequest.getStatus())) {
        totalResubmittedDrsRequests += 1;
      }
    }
    var reportingResponseObject = new ReportingResponseObject();
    reportingResponseObject.setSubmissionTotal(totalSubmissions);
    reportingResponseObject.setSuccessfulSubmission(totalSuccessfulDrsRequests);
    reportingResponseObject.setFailedSubmission(totalFailedDrsRequests);
    reportingResponseObject.setInflightSubmission(totalPublishedDrsRequests);
    reportingResponseObject.setReceivedSubmission(totalReceivedDrsRequests);
    reportingResponseObject.setResubmittedSubmission(totalResubmittedDrsRequests);
    reportingResponseObject.setFailureDetails(failureDetails);
    return reportingResponseObject;
  }

  private QueryRequestResponseObject mapSubmissionToQuerySubmissionResponseDto(
      DrsUpload drsUpload) {
    final String requestId = drsUpload.getId();
    var responseObject = new QueryRequestResponseObject();
    responseObject.setDrsUploadStatus(
        QueryRequestResponseObject.DrsUploadStatusEnum.valueOf(drsUpload.getStatus()));
    responseObject.setRequestId(requestId);
    responseObject.setDocuments(
        Optional.ofNullable(drsUpload.getDocumentIdIds())
            .map(d -> mapDocsToListOfDocumentObjectDto(d, drsUpload.getSubmissionId()))
            .orElse(null));
    return responseObject;
  }

  public List<DocumentObject> mapDocsToListOfDocumentObjectDto(
      List<DocumentId> documentIdIds, String submissionId) {
    final List<DocumentObject> documentObjectList = new ArrayList<>();
    documentIdIds.forEach(
        d -> {
          var documentation = dataService.findDocumentById(d.getDocumentId());
          Optional.ofNullable(documentation)
              .ifPresentOrElse(
                  doc -> {
                    var dot = new DocumentObject();
                    dot.setDocumentId(doc.getId());
                    dot.setSubmissionId(submissionId);
                    dot.setName(doc.getFilename());
                    dot.setContentType(doc.getDocumentType());
                    dot.setSize(doc.getSizeKb());
                    documentObjectList.add(dot);
                  },
                  () -> log.warn("Documentation not present in Mongo id {}", d.getDocumentId()));
        });
    return documentObjectList;
  }

  @SuppressWarnings("unused")
  private void attachSubmissionDocuments(
      QueryRequestResponseObject resp, List<DocumentId> documentIds, final String subId) {
    List<DocumentObject> documentObjectList =
        documentIds.stream()
            .map(
                doc -> {
                  Documentation documentation = dataService.findDocumentById(doc.getDocumentId());
                  if (documentation != null) {
                    var documentObject = new DocumentObject();
                    documentObject.setSubmissionId(subId);
                    documentObject.setName(documentation.getFilename());
                    documentObject.setSize(documentation.getSizeKb());
                    documentObject.setContentType(documentation.getDocumentType());
                    return documentObject;
                  }
                  return null;
                })
            .collect(Collectors.toList());
    documentObjectList.removeIf(Objects::isNull);
    resp.setDocuments(documentObjectList);
  }
}
