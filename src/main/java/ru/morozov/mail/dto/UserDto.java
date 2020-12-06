package ru.morozov.mail.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDto {
    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
}
