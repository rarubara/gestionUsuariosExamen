package com.example.tutoria.service;

import com.example.tutoria.entity.Usuario;
import com.example.tutoria.request.LoginRequest;
import com.example.tutoria.request.UsuarioRequest;
import com.example.tutoria.response.UsuarioResponse;

public interface UsuarioService {
    UsuarioResponse create(UsuarioRequest usuarioRequest);
    String login(LoginRequest loginRequest);
}
