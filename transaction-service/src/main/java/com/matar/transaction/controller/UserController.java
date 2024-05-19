package com.matar.transaction.controller;

import com.matar.transaction.model.user.User;
import com.matar.transaction.services.user.AuthenticationService;
import com.matar.transaction.services.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/users")
@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    private final AuthenticationService authenticationService;

    @GetMapping("/me")
    public User authenticatedUser() {
        return authenticationService.getCurrentAuthenticatedUser();
    }

    @GetMapping
    public List<User> allUsers() {
        return userService.allUsers();
    }
}
