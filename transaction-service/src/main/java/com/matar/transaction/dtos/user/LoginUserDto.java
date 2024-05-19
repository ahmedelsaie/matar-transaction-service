package com.matar.transaction.dtos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
@Schema
public class LoginUserDto {

    @Email
    @Schema(example = "x@gmail.com")
    private String email;

    @Schema(example = "password")
    private String password;

    @Override
    public String toString() {
        return "LoginUserDto{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
