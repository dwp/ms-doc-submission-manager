package uk.gov.dwp.health.pip.document.submission.manager;

import lombok.Generated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Generated
@EnableMongoRepositories(basePackages = "uk.gov.dwp.health.pip.document.submission.manager")
@SpringBootApplication(scanBasePackages = {"uk.gov.dwp.health.pip.document.submission.manager",
    "uk.gov.dwp.health.mongo.changestream.config.properties"})
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
