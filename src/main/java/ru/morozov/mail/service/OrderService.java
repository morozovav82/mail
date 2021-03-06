package ru.morozov.mail.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.morozov.mail.dto.OrderDto;

import java.util.Arrays;

@Service
@Slf4j
public class OrderService {

    @Value("${order.url}")
    private String orderUrl;

    public OrderDto getOrder(Long orderId, Long userId) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            String url = orderUrl + "/" + orderId;
            log.debug("Sent request to " + url);
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.put(UserService.USER_ID_HEADER, Arrays.asList(String.valueOf(userId)));
            HttpEntity<?> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<OrderDto> result = restTemplate.exchange(url, HttpMethod.GET, requestEntity, OrderDto.class);
            log.info("Order found. Result: {}", result.getBody());
            return result.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("Order not found by id: " + orderId);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to get order by id " + orderId, e);
        }
    }
}
