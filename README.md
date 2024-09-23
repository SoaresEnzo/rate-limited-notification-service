# [Backend] Rate-Limited Notification Service

We have a Notification system that sends out email notifications of various types (status update, daily news, project invitations, etc). We need to protect recipients from getting too many emails, either due to system errors or due to abuse, so let’s limit the number of emails sent to them by implementing a rate-limited version of NotificationService.

The system must reject requests that are over the limit.

Some sample notification types and rate limit rules, e.g.:

Status: not more than 2 per minute for each recipient

News: not more than 1 per day for each recipient

Marketing: not more than 3 per hour for each recipient

Etc. these are just samples, the system might have several rate limit rules!

## Introduction

This project was developed using Java 21 and Springboot, it runs a distributed rate limiter, so it can be scaled horizontally.
It uses Redis as a distributed cache to store the rate limits and the number of requests made by each user and Mailhog to simulate the email sending.
It also uses Localstack to simulate the AWS Secrets Manager, that in production would be used to store the credentials for the email sending service and for connecting with Redis.

## Requirements to run locally
1. Docker
2. Docker-compose


## How to run locally



1. Certify that you have Docker and Docker Compose installed on your machine.



2. Clone the project from the repository:



    ```
    git clone https://github.com/SoaresEnzo/rate-limited-notification-service.git
    ```



3. Access the project directory:



    ```
    cd rate-limited-notification-service
    ```



4. Execute the following command to start the project:



    ```
    docker-compose up -d
    ```



5. Await the project to be built and running.


6. The API will be running on ports 8080 and 8081 of your computer. You can check the API documentation to see how to call the api using the following links:



- http://localhost:8081/swagger-ui/index.html
- http://localhost:8081/v3/api-docs
- http://localhost:8080/swagger-ui/index.html
- http://localhost:8080/v3/api-docs

7. After sending a request, you can access the Mailhog interface to see the emails sent. 

- The Mailhog interface will be available at http://localhost:8025

## Configuring the rate limits

1. The rate limit configuration is provided via the 'NOTIFICATION_PARAMETERS' environment variable. 

2. The value of the 'NOTIFICATION_PARAMETERS' environment variable must be a valid JSON object with the following structure:

```json
{
  "MARKETING": {
    "rate":3,
    "limit":1,
    "period":"HOURS"
  },
  "STATUS": {
    "rate":2,
    "limit":1,
    "period":"MINUTES"
  },
  "NEWS": {
    "rate":1,
    "limit":1,
    "period":"DAYS"
  }
}
```

3. The 'rate' field represents the number of requests that can be made in a time window. The 'limit' and 'period' fields define the time window.
4. Example: For a rate of 1, a limit of 3 and a period of HOURS, the user can make 1 request every 3 hours.

## Architecture


The project follows a simplified version of clean architecture, seeking to segregate the responsibilities of the application into different layers.


![Simplified Clean Architecture](/docs/Simplified_Clean_Architecture.webp)


- `app`: Contains the application layer, responsible for providing the technical implementations of the system. It is in this layer that implementations of the system's database or external communications are located.

- `core`: It is the heart of the system, contains the entities and the orchestration of the system's business rules seeking independence from any framework.


## Cloud Architecture

Solution using AWS
![Cloud Solution](/docs/Cloud_Architecture.png)

- VPC with private subnet: Isolates the containers from the internet, granting access only through the API Gateway.
- API Gateway: Used to expose the API to the internet and to manage things like authentication and firewall.
- Load Balancer: Used to distribute the requests between the containers of the API.
- ECS Fargate: Used to run and auto-scale the containers of the API.
- Redis: Used to store the requests of the users so the rate limiter can work distributed.
- Secrets Manager: Used to store the credentials of the email sending service and the connection with Redis.
- NAT Gateway: Used to allow the containers to access the internet to send emails through outside email services.


## Possible Improvements

1. Make the connection to the Secrets Manager consider secret rotation for the Redis credentials.
2. Add Prometheus and Grafana to monitor the services.
3. Use a configuration service, like Spring Cloud Config Server to store the rate limit rules, so they can be changed without the need to redeploy the application.