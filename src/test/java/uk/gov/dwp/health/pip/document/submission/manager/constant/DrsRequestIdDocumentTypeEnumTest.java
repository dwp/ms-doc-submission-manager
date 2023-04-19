package uk.gov.dwp.health.pip.document.submission.manager.constant;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class DrsRequestIdDocumentTypeEnumTest {

  @Test
  void testGetEnumObjFromStringLiteral() {
    assertAll(
        "Test get Document type by string literal identifier",
        () -> assertThat(DrsDocumentTypeEnum.PIP2_FORM).isEqualTo(DrsDocumentTypeEnum.get("1274")),
        () ->
            assertThat(DrsDocumentTypeEnum.PIP2_EVIDENCE)
                .isEqualTo(DrsDocumentTypeEnum.get("1241")));
  }
}
