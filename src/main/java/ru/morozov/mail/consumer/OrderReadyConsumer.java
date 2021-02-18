package ru.morozov.mail.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.morozov.mail.service.MailService;
import ru.morozov.messages.OrderReadyMsg;

@Component
@Slf4j
@RequiredArgsConstructor
@RabbitListener(queues = "${mq.OrderReady-topic}")
public class OrderReadyConsumer {

    private final MailService mailService;

    @RabbitHandler
    public void receive(OrderReadyMsg msg) {
        log.info("Received Message: {}", msg.toString());
        try {
            mailService.sendOrderReadyMail(msg.getOrderId(), msg.getUserId());
        } catch (Exception e) {
            log.error("Failed to save mail", e);
        }
    }
}
