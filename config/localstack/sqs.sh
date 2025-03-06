#!/bin/bash

awslocal sqs create-queue --queue-name docbatch-batch-upload
awslocal sqs create-queue --queue-name docbatch-batch-upload-dlq

awslocal sqs create-queue --queue-name docbatch-batch-response
awslocal sqs create-queue --queue-name docbatch-batch-response-dlq

awslocal sqs create-queue --queue-name truemockdrs-test-batch-response
awslocal sqs create-queue --queue-name truemockdrs-test-batch-response-dlq

awslocal sqs create-queue --queue-name environment-config-complete

