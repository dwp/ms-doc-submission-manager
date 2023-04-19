## Strategic pip-apply ms-documentId-submission-manager 

This micro-service provides API for PIP html solution to submit PIP2 form in PDF to DRS. In addition, it prepares 
document (evidence) to be uploaded to DRS. It is a standard SpringBoot application.

## REST API Spec

The version 1 API spec can be found at [openapi-spec-v1.yaml](api-spec/openapi-spec-v1.yaml)

## API Endpoints

* Submit new application with or without further document evidence.
 
```html

/v1/apply

```

* Attach additional document evidence to an existing application.

```html 

/v1/attach

```

* Query DRS request status

```html

/v1/status

```

* Query DRS reporting data based on either day or week

```html

/v1/administration/report

```

* Resubmit the failed DRS requests

```html

/v1/administration/resubmit

```

## To build and run

```bash

mvn clean verify 

```

```bash

mvn spring-boot:run

```

## Run in a docker container

```bash

docker-compose up --scale api-test=0

```

will build and run the application with other dependent services stubbed

## Running the Component Tests

### Running Locally
Run the following command to spin up the service in docker
```bash 
docker-compose up --scale api-test=0
```
Open another terminal window and run the following maven command to execute the tests locally
```bash 
mvn clean verify -Papi-component-tests
```

### Running in Docker
Run the following command to launch the api component tests within the docker environment
```bash 
docker-compose up 
```
