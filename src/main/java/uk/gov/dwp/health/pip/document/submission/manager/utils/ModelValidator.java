package uk.gov.dwp.health.pip.document.submission.manager.utils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ModelValidator {

  private static final List<String> errorMessages = new ArrayList<>();

  public static boolean validate(Object toBeValidated) {
    errorMessages.clear();
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    Set<ConstraintViolation<Object>> errors = validator.validate(toBeValidated);
    if (!errors.isEmpty()) {
      errors.forEach(msg -> errorMessages.add(msg.getMessage()));
    }
    return errors.isEmpty();
  }

  public static String errorsToString() {
    var builder = new StringBuilder();
    errorMessages.forEach(msg -> builder.append(" ").append(msg).append(" "));
    return builder.toString().trim();
  }
}
