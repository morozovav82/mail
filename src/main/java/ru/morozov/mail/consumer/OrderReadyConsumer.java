package ru.morozov.mail.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import ru.morozov.mail.service.MailService;
import ru.morozov.messages.OrderReadyMsg;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderReadyConsumer implements MessageListener {

    private final MailService mailService;

    private ObjectMessage receiveMessage(Message message) {
        ObjectMessage objectMessage;

        try {
            objectMessage = (ObjectMessage) message;
            log.info("Received Message: {}", objectMessage.getObject().toString());
            return objectMessage;
        } catch (Exception e) {
            log.error("Failed to receive message", e);
            return null;
        }
    }

    @Override
    @JmsListener(destination = "${active-mq.OrderReady-topic}")
    public void onMessage(Message message) {
        ObjectMessage objectMessage = receiveMessage(message);
        if (objectMessage == null) return;

        try {
            OrderReadyMsg msg = (OrderReadyMsg) objectMessage.getObject();
            mailService.sendOrderReadyMail(msg.getOrderId());
        } catch (Exception e) {
            log.error("Failed to save delivery", e);
        }
    }
}
