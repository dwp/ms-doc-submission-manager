package uk.gov.dwp.health.pip.document.submission.manager.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Getter
public abstract class Validatable {

  @JsonIgnore private Set<ConstraintViolation<Object>> errors = new HashSet<>();
  @JsonIgnore private List<String> errorMessages = new ArrayList<>();

  public boolean validate() {
    final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    errors = validator.validate(this);
    if (!errors.isEmpty()) {
      errorMessages = new ArrayList<>();
      errors.forEach(e -> errorMessages.add(e.getMessage()));
    }
    return errors.isEmpty();
  }

  public String errorsToString() {
    StringBuilder builder = new StringBuilder();
    Optional.ofNullable(errorMessages).ifPresent(m -> builder.append(String.format(" %s ", m)));
    return builder.toString().trim();
  }
}
