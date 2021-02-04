package ru.morozov.mail.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.morozov.messages.OrderCanceledMsg;
import ru.morozov.messages.OrderReadyMsg;

@RestController
@RequestMapping("/tests")
@RequiredArgsConstructor
@Slf4j
public class TestController {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${active-mq.OrderReady-topic}")
    private String orderReadyTopic;

    @Value("${active-mq.OrderCanceled-topic}")
    private String orderCanceledTopic;

    private void sendMessage(String topic, Object message){
        try{
            log.info("Attempting send message to Topic: "+ topic);
            jmsTemplate.convertAndSend(topic, message);
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
