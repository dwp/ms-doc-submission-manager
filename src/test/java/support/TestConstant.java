package support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TestConstant {

  public static final ObjectMapper MAPPER = new ObjectMapper();
  public static final String APPLICATION_ID = "claim_id_001";
  public static final String CLAIMANT_ID_1 = "claimant_id_001";
  public static final String CLAIMANT_ID_2 = "claimant_id_002";
  public static final LocalDateTime TIME = LocalDateTime.of(2020, 5, 28, 11, 35, 20);
  public static final LocalDate DATE = LocalDate.of(2020, 5, 28);
  public static final String DOC_ID_1 = "doc_id_001";
  public static final String DOC_ID_2 = "doc_id_002";
  public static final String DOC_ID_3 = "doc_id_003";
  public static final String DRS_ENVP = "drs_envop_id";
  public static final String SUBMISSION_ID = "submission_id_001";
  public static final String S3_BUCKET = "s3_test_bucket";
  public static final String URL = "http://mock-url-link";
  public static final String FILE_NAME = "test.pdf";
  public static final String FILE_CONTENT = "application/pdf";
  public static final String REQUEST_ID_1 = "request_id_001";
  public static final String REQUEST_ID_2 = "request_id_002";

  static {
    MAPPER.registerModule(new JavaTimeModule());
    MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }
}
