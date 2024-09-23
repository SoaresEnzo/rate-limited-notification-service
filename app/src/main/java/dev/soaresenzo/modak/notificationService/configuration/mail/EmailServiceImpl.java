package dev.soaresenzo.modak.notificationService.configuration.mail;

import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class EmailServiceImpl implements EmailService {
    @Value("${spring.mail.properties.mail.server-email}")
    private String serverEmail;
    private final JavaMailSender javaMailSender;

    private static Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Inject
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = Objects.requireNonNull(javaMailSender);
    }

    @Override
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(this.serverEmail);

        javaMailSender.send(message);
        logger.info("Email sent successfully to: {}", to);
    }

    public String getServerEmail() {
        return serverEmail;
    }
}
