package dev.soaresenzo.modak.notificationService.notification.facade;

import dev.soaresenzo.modak.notificationService.configuration.mail.EmailService;
import dev.soaresenzo.modak.notificationService.configuration.mail.EmailServiceImpl;
import jakarta.inject.Inject;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "SECRETS_MANAGER_ENDPOINT=http://localhost:4566"
})
class NotificationGatewayImplTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    @Test
    void givenAValidInput_whenSendEmail_thenShouldSendEmail() {
        final var anAddress = "test@mail.com";
        final var aSubject = "new email";
        final var aBody = "body of new email";

        emailService.sendEmail(anAddress, aSubject, aBody);

        Mockito.verify(javaMailSender, Mockito.times(1)).send(
                new SimpleMailMessage() {{
                    setTo(anAddress);
                    setSubject(aSubject);
                    setText(aBody);
                    setFrom(emailService.getServerEmail());
                }}
        );


    }
}