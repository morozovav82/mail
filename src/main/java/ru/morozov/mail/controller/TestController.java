package ru.morozov.mail.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import ru.morozov.mail.entity.Mail;
import ru.morozov.mail.repo.MailRepository;
import ru.morozov.messages.OrderCanceledMsg;
import ru.morozov.messages.OrderReadyMsg;

import java.util.List;

@RestController
@RequestMapping("/tests")
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final RabbitTemplate rabbitTemplate;
    private final MailRepository mailRepository;

    @Value("${mq.OrderReady-topic}")
    private String orderReadyTopic;

    @Value("${mq.OrderCanceled-topic}")
    private String orderCanceledTopic;

    @GetMapping("/list")
    public List<Mail> getAll() {
        return mailRepository.findAll();
    }

    private void sendMessage(String topic, Object message){
        try{
            log.info("Attempting send message to Topic: "+ topic);
            rabbitTemplate.convertAndSend(topic, message);
            log.info("Message sent: {}", message);
        } catch(Exception e){
            log.error("Failed to send message", e);
        }
    }

    @PostMapping("/sendOrderReadyMsg")
    public void sendOrderReadyMsg(@RequestBody OrderReadyMsg message) {
        sendMessage(orderReadyTopic, message);
    }

    @PostMapping("/sendOrderCanceledMsg")
    public void sendOrderCanceledMsg(@RequestBody OrderCanceledMsg message) {
        sendMessage(orderCanceledTopic, message);
    }
}
