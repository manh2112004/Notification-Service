package org.Notification.command.service;

public interface SmsService {
    void sendSms(String phoneNumber, String message);
}
