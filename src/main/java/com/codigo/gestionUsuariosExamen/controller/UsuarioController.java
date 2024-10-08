package com.codigo.gestionUsuariosExamen.controller;

import com.codigo.gestionUsuariosExamen.dao.UsuarioRepository;
import com.codigo.gestionUsuariosExamen.entity.Usuario;
import com.codigo.gestionUsuariosExamen.security.AuthenticationResponse;
import com.codigo.gestionUsuariosExamen.service.JwtService;
import com.codigo.gestionUsuariosExamen.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UsuarioController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    @Qualifier("userDetailsServiceImpl")
    private UserDetailsService userDetailsService;

    @GetMapping("/hola")
    public String hola() {
        return "Hola usuario";
    }

    @GetMapping("/holaAdmin")
    public String holaAdmin() {
        return "Hola admin";
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(
            @RequestHeader("id") String id,
            @RequestHeader("password") String password) {

        try {
            // Authenticate the user using the dni and password
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(id, password));

            // If authentication is successful, generate the JWT token
            Optional<Usuario> usuario = usuarioRepository.findByNumeroDocumento(id);
            List<String> roles = List.of(usuario.get().getRole().getNameRole());

            UserDetails userDetails = userDetailsService.loadUserByUsername(id);

            String jwt = jwtService.generateToken(userDetails);

            // Return the JWT token as the response
            return ResponseEntity.ok(new AuthenticationResponse(jwt));
        } catch (BadCredentialsException e) {
            // Handle invalid credentials
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUsuario(@RequestBody Usuario usuario, @RequestHeader String password){
        usuario.setPassword(password);
        return ResponseEntity.ok(usuarioService.create(usuario));
    }

    @GetMapping("")
    public ResponseEntity<List<Usuario>> readUsuarios(){
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @GetMapping("/find/{id}")
    @Cacheable(value = "usersCache", key = "#numeroDocumento")
    public ResponseEntity<List<Usuario>> readUsuarios(@PathVariable ("id") String numeroDocumento){
        return ResponseEntity.ok(List.of(usuarioRepository.findByNumeroDocumento(numeroDocumento).orElse(new Usuario())));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable("id") String numeroDocumento,
                                                       @RequestBody  Usuario usuario){

        Optional<Usuario> usuarioExistente = usuarioRepository.findByNumeroDocumento(numeroDocumento);
        if(usuarioExistente.isPresent()){
            Usuario datosActualizar = usuarioExistente.get();
            datosActualizar.setId(usuario.getId());
            datosActualizar.setNombres(usuario.getNombres());
            datosActualizar.setApellidoPaterno(usuario.getApellidoPaterno());
            datosActualizar.setApellidoMaterno(usuario.getApellidoMaterno());
            datosActualizar.setTipoDocumento(usuario.getTipoDocumento());
            datosActualizar.setNumeroDocumento(usuario.getNumeroDocumento());
            datosActualizar.setDigitoVerificador(usuario.getDigitoVerificador());
            datosActualizar.setEmail(usuario.getEmail());
            return ResponseEntity.ok(usuarioRepository.save(datosActualizar));
        }else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new Usuario());
        }
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUsuario(@PathVariable("id") Long id) {
        usuarioRepository.deleteById(id);
    }
}
