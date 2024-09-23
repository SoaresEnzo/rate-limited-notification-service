package dev.soaresenzo.modak.notificationService.notification.entrypoint.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.soaresenzo.modak.notificationService.RedisCleanUpExtension;
import dev.soaresenzo.modak.notificationService.TestRedisConfiguration;
import dev.soaresenzo.modak.notificationService.configuration.exception.NotConfiguredException;
import dev.soaresenzo.modak.notificationService.configuration.mail.EmailService;
import dev.soaresenzo.modak.notificationService.configuration.mail.EmailServiceImpl;
import dev.soaresenzo.modak.notificationService.configuration.parameters.NotificationConfiguration;
import dev.soaresenzo.modak.notificationService.configuration.parameters.NotificationProperties;
import dev.soaresenzo.modak.notificationService.notification.entrypoint.rest.requests.SendNotificationRequest;
import dev.soaresenzo.modak.notificationService.notification.usecases.SendNotificationUseCase;
import dev.soaresenzo.modak.notificationService.notification.valueobjects.NotificationType;
import dev.soaresenzo.modak.notificationService.rateLimit.dataprovider.RateLimitKey;
import dev.soaresenzo.modak.notificationService.rateLimit.dataprovider.RateLimitStoreEntry;
import dev.soaresenzo.modak.notificationService.rateLimiter.RateLimitConfigurable;
import dev.soaresenzo.modak.notificationService.rateLimiter.RateLimitSubject;
import dev.soaresenzo.modak.notificationService.rateLimiter.exceptions.RateLimitExceededException;
import io.awspring.cloud.autoconfigure.config.secretsmanager.SecretsManagerConfigDataLoader;
import jakarta.inject.Inject;
import jakarta.mail.internet.MimeMessage;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestRedisConfiguration.class)
@ExtendWith(RedisCleanUpExtension.class)
@ActiveProfiles("test")
class NotificationRestControllerTest {

    @Inject
    private MockMvc mockMvc;

    @Inject
    private ObjectMapper objectMapper;

    @Inject
    private RedisTemplate<String, RateLimitStoreEntry> redisTemplate;

    @SpyBean
    private JavaMailSender javaMailSender;


    @Test
    void givenAValidRequest_whenSendNotification_thenShouldReturnCreated() throws Exception {
        // given
        final var aChannel = "EMAIL";
        final var aRecipient = "test@mail.com";
        final var aSubject = "Test subject";
        final var aBody = "Test body";
        final var aType = "MARKETING";
        final var anInput = new SendNotificationRequest(aChannel, aType, aRecipient, aBody, aSubject);


        Mockito.doNothing().when(javaMailSender).send(Mockito.any(SimpleMailMessage.class));

        // when
        final var request = MockMvcRequestBuilders.post("/notification/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(anInput));

        final var response = mockMvc.perform(request);

        // then
        response
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", Matchers.nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void givenAValidRequest_whenRateLimited_thenShouldReturnTooManyRequests() throws Exception {
        // given
        final var aChannel = "EMAIL";
        final var aRecipient = "test@mail.com";
        final var aSubject = "Test subject";
        final var aBody = "Test body";
        final var aType = "MARKETING";
        final var anInput = new SendNotificationRequest(aChannel, aType, aRecipient, aBody, aSubject);

        Mockito.doNothing().when(javaMailSender).send(Mockito.any(SimpleMailMessage.class));

        this.createPreSavedRequests(10L, aType, aRecipient);

        // when
        final var request = MockMvcRequestBuilders.post("/notification/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(anInput));

        final var response = mockMvc.perform(request);

        // then
        response
                .andExpect(status().isTooManyRequests())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message").value("Too many requests."));
    }

    @Test
    void givenAnInvalidType_whenSendNotification_thenShouldReturnBadRequest() throws Exception {
        // given
        final var aChannel = "EMAIL";
        final var aRecipient = "test@mail.com";
        final var aSubject = "Test subject";
        final var aBody = "Test body";
        final var aType = "invalidtype";
        final var anInput = new SendNotificationRequest(aChannel, aType, aRecipient, aBody, aSubject);
        final var expectedExceptionMessage = "Notification type not found. Available types: [MARKETING, STATUS, UPDATE]";

        Mockito.doNothing().when(javaMailSender).send(Mockito.any(SimpleMailMessage.class));
        // when
        final var request = MockMvcRequestBuilders.post("/notification/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(anInput));

        final var response = mockMvc.perform(request);

        // then
        response
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message").value(expectedExceptionMessage));
    }


    @Test
    void givenAnInvalidRecipient_whenSendNotification_thenShouldReturnBadRequest() throws Exception {
        // given
        final var aChannel = "EMAIL";
        final var aRecipient = "";
        final var aSubject = "Test subject";
        final var aBody = "Test body";
        final var aType = "MARKETING";
        final var anInput = new SendNotificationRequest(aChannel, aType, aRecipient, aBody, aSubject);
        final var expectedExceptionMessage = "The email does not meet the required format.";

        // when

        final var request = MockMvcRequestBuilders.post("/notification/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(anInput));

        final var response = mockMvc.perform(request);

        // then
        response
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message").value(expectedExceptionMessage));
    }

    private void createPreSavedRequests(Long amount, String aKey, String aRecipient) {
        final var key = RateLimitKey.generate(aKey, aRecipient);

        for (int i = 0; i < amount; i++) {
            final var entry = RateLimitStoreEntry.newEntry(
                    UUID.randomUUID().toString(),
                    aRecipient,
                    aKey
            );
            this.redisTemplate.opsForZSet().add(key, entry, Instant.now().toEpochMilli());
        }
    }
}