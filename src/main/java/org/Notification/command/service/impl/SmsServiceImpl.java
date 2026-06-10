package org.Notification.command.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.Notification.command.service.SmsService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    @Override
    public void sendSms(String phoneNumber, String message) {
        log.info("Sending SMS to phone number [{}]: \"{}\"", phoneNumber, message);
        // Trong thực tế, bạn sẽ tích hợp cổng dịch vụ SMS (Twilio, SpeedSMS, Vonage...) tại đây.
    }
}
