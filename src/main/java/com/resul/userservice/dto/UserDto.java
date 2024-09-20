package com.resul.userservice.dto;

import com.resul.userservice.entity.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String username;
    private String email;
    private String password;
    private UserRole role;
}
