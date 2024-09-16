package com.codigo.gestionUsuariosExamen.service.impl;

import com.codigo.gestionUsuariosExamen.entity.Usuario;
import com.codigo.gestionUsuariosExamen.dao.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByNumeroDocumento(username);

        if(usuarioOptional.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        Usuario usuario = usuarioOptional.get();
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(usuario.getRole().getNameRole()));
        return new User(usuario.getEmail(), usuario.getPassword(), authorities);
    }
}
