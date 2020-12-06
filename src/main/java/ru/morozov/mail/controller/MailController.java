package ru.morozov.mail.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.morozov.mail.entity.Mail;
import ru.morozov.mail.repo.MailRepository;

import java.util.List;

@RestController()
@RequiredArgsConstructor
public class MailController {

    private final MailRepository mailRepository;

    @GetMapping("/list")
    public List<Mail> getAll() {
        return mailRepository.findAll();
    }
}
