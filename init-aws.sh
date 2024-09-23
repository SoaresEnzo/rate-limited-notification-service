#!/bin/bash

# Wait for LocalStack to be ready
until $(curl --output /dev/null --silent --head --fail http://localhost:4566); do
    printf '.'
    sleep 5
done

# Create secrets
awslocal secretsmanager create-secret --region us-east-1 --name /secret/db-credential --secret-string '{"host": "redis", "port": "6379", "username": "default", "password": ""}'
awslocal secretsmanager create-secret --region us-east-1 --name /secret/email-credential --secret-string '{"emailhost": "mailhog", "emailport": "1025", "serveremail": "notifications@localhost", "emailpassword": "password", "emailusername": ""}'

echo 'Secrets created successfully.'
