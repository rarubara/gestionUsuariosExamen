package com.codigo.gestionUsuariosExamen.service;

import com.codigo.gestionUsuariosExamen.request.LoginRequest;
import com.codigo.gestionUsuariosExamen.request.UsuarioRequest;
import com.codigo.gestionUsuariosExamen.response.UsuarioResponse;



public interface UsuarioService {
    UsuarioResponse create(UsuarioRequest usuarioRequest);
    String login(LoginRequest loginRequest);
}
