package org.Notification.command.service;

public interface MailService {
    void sendHtmlEmail(String to, String subject, String htmlBody);
}
