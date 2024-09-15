package com.example.tutoria.service.impl;

import com.example.tutoria.entity.Role;
import com.example.tutoria.entity.Usuario;
import com.example.tutoria.repository.RoleRepository;
import com.example.tutoria.repository.UsuarioRepository;
import com.example.tutoria.request.LoginRequest;
import com.example.tutoria.request.UsuarioRequest;
import com.example.tutoria.response.UsuarioResponse;
import com.example.tutoria.service.JwtService;
import com.example.tutoria.service.UsuarioService;
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
    public UsuarioResponse create(UsuarioRequest usuarioRequest) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(usuarioRequest.getEmail());
        Optional<Role> roleOptional = roleRepository.findByNameRole(usuarioRequest.getRole());
        if (usuarioOptional.isPresent() || roleOptional.isEmpty()) {
            return null;
        }
        Usuario usuario = new Usuario();
        usuario.setEmail(usuarioRequest.getEmail());
        usuario.setPassword(new BCryptPasswordEncoder().encode(usuarioRequest.getPassword()));
        usuario.setRole(roleOptional.get());
        usuarioRepository.save(usuario);
        return new UsuarioResponse(usuario.getEmail(),
                usuario.getRole().getNameRole());
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
