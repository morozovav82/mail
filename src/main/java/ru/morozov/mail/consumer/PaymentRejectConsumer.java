package ru.morozov.mail.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import ru.morozov.bill.messages.PaymentRejectMsg;
import ru.morozov.mail.dto.OrderDto;
import ru.morozov.mail.dto.UserDto;
import ru.morozov.mail.entity.Mail;
import ru.morozov.mail.repo.MailRepository;
import ru.morozov.mail.service.OrderService;
import ru.morozov.mail.service.UserService;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentRejectConsumer implements MessageListener {

    private final MailRepository mailRepository;
    private final OrderService orderService;
    private final UserService userService;

    @Override
    @JmsListener(destination = "${active-mq.payment-reject-topic}")
    public void onMessage(Message message) {
        ObjectMessage objectMessage;

        try {
            objectMessage = (ObjectMessage) message;
            log.info("Received Message: {}", objectMessage.getObject().toString());
        } catch (Exception e) {
            log.error("Failed to receive message", e);
            return;
        }

        if (objectMessage != null) {
            try {
                PaymentRejectMsg orderMsg = (PaymentRejectMsg) objectMessage.getObject();

                //get order
                OrderDto order = orderService.getOrder(orderMsg.getOrderId());
                Assert.notNull(order, "Order is empty");
                Assert.notNull(order.getUserId(), "UserId is empty");

                //get user
                UserDto user = userService.getUser(order.getUserId());
                Assert.notNull(user, "User is empty");

                //create mail
                Mail mail = new Mail();
                mail.setTitle("Заказ отменен");
                mail.setEmail(user.getEmail());
                mail.setBody(
                        "Уважаемый(ая), " + user.getLastname() + " " + user.getFirstname() + "!\n" +
                        "Ваш заказ №" + order.getId() + " отменен."
                        );

                //save mail
                mail = mailRepository.save(mail);
                log.info("Mail saved. MailId=" + mail.getId());
            } catch (Exception e) {
                log.error("Failed to save mail", e);
            }
        }
    }
}
