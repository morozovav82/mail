package ru.morozov.mail.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.morozov.mail.dto.OrderDto;
import ru.morozov.mail.dto.UserDto;
import ru.morozov.mail.entity.Mail;
import ru.morozov.mail.repo.MailRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

    private final MailRepository mailRepository;
    private final OrderService orderService;
    private final UserService userService;

    public void sendOrderReadyMail(Long orderId) {
        try {
            //get order
            OrderDto order = orderService.getOrder(orderId);
            Assert.notNull(order, "Order is empty");
            Assert.notNull(order.getUserId(), "UserId is empty");

            //get user
            UserDto user = userService.getUser(order.getUserId());
            Assert.notNull(user, "User is empty");

            //create mail
            Mail mail = new Mail();
            mail.setTitle("Заказ успешно оплачен");
            mail.setEmail(user.getEmail());
            mail.setBody(
                    "Уважаемый(ая), " + user.getLastname() + " " + user.getFirstname() + "!\n" +
                            "Ваш заказ №" + order.getId() + " подтвержден и оплачен, ждите курьера."
            );

            //save mail
            mail = mailRepository.save(mail);
            log.info("Mail saved. MailId=" + mail.getId());
        } catch (Exception e) {
            log.error("Failed to send mail", e);
        }
    }

    public void sendOrderCanceled(Long orderId) {
        try {
            //get order
            OrderDto order = orderService.getOrder(orderId);
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
                            "Ваш заказ №" + order.getId() + " отменен, попробуйте сделать заказ снова."
            );

            //save mail
            mail = mailRepository.save(mail);
            log.info("Mail saved. MailId=" + mail.getId());
        } catch (Exception e) {
            log.error("Failed to send mail", e);
        }
    }
}
