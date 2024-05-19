package com.matar.transaction.dtos.user;

import com.matar.transaction.model.user.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class RegisterUserDto {

    @Email
    @Schema(example = "x@gmail.com")
    private String email;

    @NotBlank
    @Size(min = 6, max = 50)
    @Schema(example = "password")
    private String password;

    @NotBlank
    @Size(min = 5, max = 50)
    @Schema(example = "ahmed mohammed")
    private String fullName;

    @NotNull
    @Schema(example = "ADMIN")
    private UserRole userRole;

    @Override
    public String toString() {
        return "RegisterUserDto{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
