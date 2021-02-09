package ru.morozov.mail.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.morozov.mail.service.MailService;
import ru.morozov.messages.OrderCanceledMsg;

@Component
@Slf4j
@RequiredArgsConstructor
@RabbitListener(queues = "${active-mq.OrderCanceled-topic}")
public class OrderCanceledConsumer {

    private final MailService mailService;

    @RabbitHandler
    public void receive(OrderCanceledMsg msg) {
        log.info("Received Message: {}", msg.toString());
        try {
            mailService.sendOrderCanceled(msg.getOrderId());
        } catch (Exception e) {
            log.error("Failed to save mail", e);
        }
    }
}
