package ru.morozov.mail.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.morozov.mail.dto.UserDto;

@Service
@Slf4j
public class UserService {

    @Value("${users.url}")
    private String usersUrl;

    public UserDto getUser(Long userId) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = usersUrl + "/user/" + userId;
            log.debug("Sent request to " + url);
            ResponseEntity<UserDto> result = restTemplate.getForEntity(url, UserDto.class);
            log.info("User found. Result: {}", result.getBody());
            return result.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("User not found by id: " + userId);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to find user by id: " + userId, e);
        }
    }
}
