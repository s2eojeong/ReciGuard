package com.ReciGuard.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPasswordDTO {
    private String username;
    private String currentPassword;
    private String newPassword;
}