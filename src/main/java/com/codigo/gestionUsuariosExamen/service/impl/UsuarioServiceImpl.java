package com.codigo.gestionUsuariosExamen.service.impl;

import com.codigo.gestionUsuariosExamen.entity.Role;
import com.codigo.gestionUsuariosExamen.entity.Usuario;
import com.codigo.gestionUsuariosExamen.dao.RoleRepository;
import com.codigo.gestionUsuariosExamen.dao.UsuarioRepository;
import com.codigo.gestionUsuariosExamen.request.LoginRequest;
import com.codigo.gestionUsuariosExamen.request.UsuarioRequest;
import com.codigo.gestionUsuariosExamen.response.UsuarioResponse;
import com.codigo.gestionUsuariosExamen.service.JwtService;
import com.codigo.gestionUsuariosExamen.service.UsuarioService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private final RoleRepository roleRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, AuthenticationManager authenticationManager, JwtService jwtService, UserDetailsServiceImpl userDetailsService, RoleRepository roleRepository) {
        this.usuarioRepository = usuarioRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.roleRepository = roleRepository;
    }

    @Override
    public Usuario create(Usuario usuario) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByNumeroDocumento(usuario.getNumeroDocumento());
        Optional<Role> roleOptional = roleRepository.findByNameRole((usuario.getRole().getNameRole()));
        if (usuarioOptional.isPresent() || roleOptional.isEmpty()) {
            return null;
        }
        //Usuario usuario = new Usuario();
        //usuario.setEmail(usuario.getEmail());
        usuario.setPassword(new BCryptPasswordEncoder().encode(usuario.getPassword()));
        usuario.setRole(roleOptional.get());
        //usuario.setRole(roleOptional.get());
        usuarioRepository.save(usuario);
        return usuario;
    }

    @Override
    public String login(LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        if (auth.isAuthenticated()) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
            return jwtService.generateToken(userDetails);
        }
        return null;
    }
}
