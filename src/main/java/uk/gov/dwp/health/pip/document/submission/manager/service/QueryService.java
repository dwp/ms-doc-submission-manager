package uk.gov.dwp.health.pip.document.submission.manager.service;

import uk.gov.dwp.health.pip.document.submission.manager.openapi.model.QueryRequestResponseObject;
import uk.gov.dwp.health.pip.document.submission.manager.openapi.model.ReportingResponseObject;

public interface QueryService {

  QueryRequestResponseObject queryRequestStatusById(String queryRequest);

  ReportingResponseObject getReportingData(String dayOrWeek);
}
