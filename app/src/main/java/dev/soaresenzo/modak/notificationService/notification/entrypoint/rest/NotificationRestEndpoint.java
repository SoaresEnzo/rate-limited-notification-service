package dev.soaresenzo.modak.notificationService.notification.entrypoint.rest;

import dev.soaresenzo.modak.notificationService.notification.entrypoint.rest.requests.SendNotificationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/notification")
public interface NotificationRestEndpoint {

    @PostMapping("/send")
    ResponseEntity<?> sendNotification(@RequestBody SendNotificationRequest request);
}
