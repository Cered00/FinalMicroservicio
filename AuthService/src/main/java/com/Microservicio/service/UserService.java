package com.Microservicio.service;

import java.util.List;
import java.util.Optional;

import com.Microservicio.model.User;
import com.Microservicio.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(User usuario) {
        return userRepository.save(usuario);
    }

    public User findById(int idUsuario) {
        Optional<User> usuario = userRepository.findById(idUsuario);
        return usuario.orElse(null);
    }

    public User findByUsername(String username){
        Optional<User> usuario = userRepository.findByUsername(username);
        return usuario.orElse(null);
    }

    public User register(String username, String password, String rol) {
        User usuario = new User();
        usuario.setUsername(username);
        usuario.setPassword(password); 
        usuario.setRol(rol);
        return userRepository.save(usuario);
    }

    public String login(String username, String password) throws Exception { 
        Optional<User> usuarioOpt = userRepository.findByUsername(username);

        if (usuarioOpt.isEmpty()) {
            throw new Exception("Usuario no encontrado");
        }

        User user = usuarioOpt.get();

        if (!user.getPassword().equals(password)) {
            throw new Exception("Contraseña incorrecta");
        }
        String token = "fake_jwt_token_for_" + user.getUsername() + "_" + System.currentTimeMillis();
        
        return token; // Retorna el token como un String
    }
}