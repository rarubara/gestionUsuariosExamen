package com.codigo.gestionUsuariosExamen.service;

import com.codigo.gestionUsuariosExamen.entity.Usuario;
import com.codigo.gestionUsuariosExamen.request.LoginRequest;
import com.codigo.gestionUsuariosExamen.request.UsuarioRequest;
import com.codigo.gestionUsuariosExamen.response.UsuarioResponse;

import javax.management.relation.Relation;


public interface UsuarioService {
    Usuario create(Usuario usuario);
    String login(LoginRequest loginRequest);

}
