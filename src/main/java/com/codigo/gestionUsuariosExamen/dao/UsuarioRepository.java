package com.codigo.gestionUsuariosExamen.dao;

import com.codigo.gestionUsuariosExamen.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByNumeroDocumento(String numeroDocumento);
}
