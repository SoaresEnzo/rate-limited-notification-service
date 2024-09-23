package dev.soaresenzo.modak.notificationService.configuration.mail;

public interface EmailService {

    void sendEmail(String to, String subject, String text);
}
