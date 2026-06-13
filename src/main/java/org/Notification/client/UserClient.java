package org.Notification.client;

import lombok.extern.slf4j.Slf4j;
import org.Notification.client.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public List<String> getAllUserIds(String token) {
        String url = "http://user-service/api/v1/admin/users?isActive=true&size=99999";
        try {
            HttpHeaders headers = new HttpHeaders();
            if (token != null && !token.isBlank()) {
                headers.setBearerAuth(token);
            }

            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<java.util.Map> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, java.util.Map.class);
            java.util.Map response = responseEntity.getBody();

            if (response == null || !response.containsKey("content")) {
                return Collections.emptyList();
            }
            List<java.util.Map> content = (List<java.util.Map>) response.get("content");
            return content.stream()
                    .map(user -> (String) user.get("keycloakUid"))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error calling User Service to get active users", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Không thể lấy danh sách người dùng hoạt động từ User Service");
        }
    }

    public String getUserEmail(String userId, String token) {
        String url = "http://user-service/api/v1/admin/users/" + userId;
        try {
            HttpHeaders headers = new HttpHeaders();
            if (token != null && !token.isBlank()) {
                headers.setBearerAuth(token);
            }
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<java.util.Map> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, java.util.Map.class);
            java.util.Map response = responseEntity.getBody();
            if (response != null && response.containsKey("email")) {
                return (String) response.get("email");
            }
            return null;
        } catch (Exception e) {
            log.error("Error calling User Service to get user email: userId={}", userId, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Không thể lấy email người dùng từ User Service");
        }
    }

    public String getUserPhoneNumber(String userId) {
        String url = "http://profile-service/api/v1/profiles/public/user/" + userId;
        try {
            java.util.Map response = restTemplate.getForObject(url, java.util.Map.class);
            if (response != null && response.containsKey("phoneNumber")) {
                return (String) response.get("phoneNumber");
            }
            return null;
        } catch (Exception e) {
            log.error("Error calling Profile Service to get phone number: userId={}", userId, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Không thể lấy số điện thoại từ Profile Service");
        }
    }
}
