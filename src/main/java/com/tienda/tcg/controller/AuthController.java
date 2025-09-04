package com.tienda.tcg.controller;

import com.tienda.tcg.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
            @RequestParam String email,
            @RequestParam String password) {
        userService.registerUser(username, email, password);
        return "Usuario registrado con éxito";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
            @RequestParam String password) {
        boolean success = userService.authenticate(username, password);
        return success ? "Login exitoso" : "Usuario o contraseña incorrectos";
    }
}
