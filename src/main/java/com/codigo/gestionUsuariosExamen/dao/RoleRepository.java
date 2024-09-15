package com.codigo.gestionUsuariosExamen.dao;

import com.codigo.gestionUsuariosExamen.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByNameRole(String name);
}
