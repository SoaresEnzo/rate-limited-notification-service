package dev.soaresenzo.modak.notificationService.rateLimiter.usecases.impl;

import dev.soaresenzo.modak.notificationService.notification.Notification;
import dev.soaresenzo.modak.notificationService.rateLimiter.dataprovider.RateLimitGateway;
import dev.soaresenzo.modak.notificationService.rateLimiter.exceptions.RateLimitExceededException;
import dev.soaresenzo.modak.notificationService.rateLimiter.usecases.ApplyRateLimitUsecase;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.Objects;


@Named
public class ApplyRateLimitUsecaseImpl implements ApplyRateLimitUsecase {
    private final RateLimitGateway gateway;

    @Inject
    public ApplyRateLimitUsecaseImpl(final RateLimitGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public void apply(Notification notification, Runnable function) {
        final var maxRequests = notification.getRateLimitData().getAmountOfRequests();

        final var processedRequests = gateway.getAmountOfRequestsInTheLastPeriodForRecipient(notification.getRecipient(), notification.getRateLimitData());

        if (processedRequests >= maxRequests) {
            throw new RateLimitExceededException("Too many requests.");
        }

        this.gateway.saveRequest(notification.getId(), notification.getRecipient(), notification.getRateLimitData());
        try {
        function.run();
        } catch (Exception e) {
            this.gateway.removeRequest(notification.getId(), notification.getRecipient(), notification.getRateLimitData());
            throw e;
        }
    }
}
