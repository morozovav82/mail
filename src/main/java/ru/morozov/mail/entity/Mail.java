package ru.morozov.mail.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "mails")
@Getter
@Setter
public class Mail {

    @Id
    @SequenceGenerator(name="mails_gen", sequenceName="mails_id_seq", allocationSize = 1)
    @GeneratedValue(strategy=SEQUENCE, generator="mails_gen")
    private Long id;

    private String title;
    private String email;
    private String body;
}
