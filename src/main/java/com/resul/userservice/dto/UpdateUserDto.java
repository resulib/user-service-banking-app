package com.resul.userservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDto {
    private String username;
    private String email;
    private String password;
    private boolean isDeleted;
}