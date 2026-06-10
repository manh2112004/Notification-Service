package org.Notification.client;

import lombok.extern.slf4j.Slf4j;
import org.Notification.client.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Component
@Slf4j
public class UserClient {

    @Autowired
    private RestTemplate restTemplate;

    public boolean checkUserExists(String userId) {
        String url = "http://profile-service/api/v1/profiles/public/user/" + userId;
        try {
            restTemplate.getForObject(url, UserResponse.class);
            return true;
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.warn("User profile not found in Profile-Service: userId={}", userId);
                return false;
            }
            log.error("HTTP error calling Profile Service: status={}, body={}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Không thể xác thực thông tin người dùng từ Profile Service");
        } catch (Exception e) {
            log.error("Error calling Profile Service to check user: userId={}", userId, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Không thể kết nối đến Profile Service");
        }
    }
}
