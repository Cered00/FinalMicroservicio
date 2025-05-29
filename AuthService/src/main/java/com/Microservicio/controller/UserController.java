package com.Microservicio.controller;

import com.Microservicio.model.User;
import com.Microservicio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        List<User> usuarios = userService.findAll();
        return usuarios.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createUsuario(@RequestBody User usuario) {
        try {
            if (userService.findByUsername(usuario.getUsername()) != null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Username already exists");
                return new ResponseEntity<>(error, HttpStatus.CONFLICT);
            }
            User nuevoUsuario = userService.save(usuario);
            return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<User> getById(@PathVariable int idUsuario) {
        User usuario = userService.findById(idUsuario);
        return usuario != null
                ? new ResponseEntity<>(usuario, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrarUsuario(
            @RequestBody(required = false) RegisterRequest body,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String rol) {
        try {
            String finalUsername = body != null && body.getUsername() != null ? body.getUsername() : username;
            String finalPassword = body != null && body.getPassword() != null ? body.getPassword() : password;
            String finalRol = body != null && body.getRol() != null ? body.getRol() : rol;

            if (finalUsername == null || finalPassword == null || finalRol == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Username, password, and rol are required");
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }

            User usuarioGuardado = userService.register(finalUsername, finalPassword, finalRol);
            return new ResponseEntity<>(usuarioGuardado, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody(required = false) LoginRequest body,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String password) {
        try {
            String finalUsername = body != null && body.getUsername() != null ? body.getUsername() : username;
            String finalPassword = body != null && body.getPassword() != null ? body.getPassword() : password;

            if (finalUsername == null || finalPassword == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Username and password are required");
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }

            String token = userService.login(finalUsername, finalPassword);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }
    }
}

class RegisterRequest {
    private String username;
    private String password;
    private String rol;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}

class LoginRequest {
    private String username;
    private String password;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}