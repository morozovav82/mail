package ru.morozov.mail.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.morozov.mail.entity.Mail;

public interface MailRepository extends JpaRepository<Mail, Long> {
}
