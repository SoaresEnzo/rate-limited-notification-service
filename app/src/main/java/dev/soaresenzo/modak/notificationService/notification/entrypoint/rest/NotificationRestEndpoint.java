package dev.soaresenzo.modak.notificationService.notification.entrypoint.rest;

import dev.soaresenzo.modak.notificationService.configuration.exception.ErrorResponse;
import dev.soaresenzo.modak.notificationService.notification.entrypoint.rest.requests.SendNotificationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Notification")
@RequestMapping("/notification")
public interface NotificationRestEndpoint {

    @PostMapping(
            value = "/send",
            consumes = "application/json",
            produces = "application/json"
    )
    @Operation(
            summary = "Send a notification",
            description = "Send a notification to a recipient"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Notification sent",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "Bad request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "429",
                    description = "Rate limit exceeded",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    ResponseEntity<?> sendNotification(@RequestBody SendNotificationRequest request);
}
