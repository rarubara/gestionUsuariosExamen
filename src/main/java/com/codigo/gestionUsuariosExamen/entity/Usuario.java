package com.codigo.gestionUsuariosExamen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String tipoDocumento;
    private String numeroDocumento;
    private String digitoVerificador;

    private String email;
    private String password; // Este campo será utilizado para la autenticación con Spring Security

    // Define roles como "USER", "ADMIN", etc. para aplicar seguridad con Spring Security
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
