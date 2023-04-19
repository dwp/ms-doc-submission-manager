package uk.gov.dwp.health.pip.document.submission.manager.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import static uk.gov.dwp.health.pip.document.submission.manager.utils.EnvironmentUtil.getEnv;

public class MongoClientConnection {
  public static MongoTemplate getMongoTemplate() {
    ConnectionString connectionString =
        new ConnectionString(
            "mongodb://"
                + getEnv("MONGODB_HOST", "localhost")
                + ":"
                + getEnv("`c`", "27017")
                + "/pip-apply-acc-mgr");

    MongoClientSettings mongoClientSettings =
        MongoClientSettings.builder().applyConnectionString(connectionString).build();

    MongoClient mongoClient = MongoClients.create(mongoClientSettings);

    return new MongoTemplate(mongoClient, "test");
  }

  public static void emptyMongoCollections() {
    MongoCollection<Document> documentCollection = getMongoTemplate().getCollection("document");
    MongoCollection<Document> drsUploadCollection = getMongoTemplate().getCollection("drs_upload");
    MongoCollection<Document> submissionCollection = getMongoTemplate().getCollection("submission");
    documentCollection.drop();
    drsUploadCollection.drop();
    submissionCollection.drop();
  }
}
