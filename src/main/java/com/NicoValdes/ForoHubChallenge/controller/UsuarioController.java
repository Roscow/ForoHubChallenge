package com.NicoValdes.ForoHubChallenge.controller;
import com.NicoValdes.ForoHubChallenge.DTOs.UsuarioDTO;
import com.NicoValdes.ForoHubChallenge.models.Usuario;
import com.NicoValdes.ForoHubChallenge.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private UsuarioDTO convertToDTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreoElectronico(),
                usuario.getContrasena()
        );
    }

    private Usuario convertToEntity(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioDTO.nombre());
        usuario.setCorreoElectronico(usuarioDTO.correoElectronico());
        usuario.setContrasena(usuarioDTO.contrasena());
        return usuario;
    }

    @GetMapping
    public List<Usuario> getAllUsuarios() {

        return usuarioRepository.findAll();

    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Integer id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Object> createUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO, UriComponentsBuilder uriBuilder) {
        try {
            Usuario usuario = convertToEntity(usuarioDTO);
            Usuario savedUsuario = usuarioRepository.save(usuario);
            URI url = uriBuilder.path("/usuarios/{id}").buildAndExpand(savedUsuario.getId()).toUri();
            return ResponseEntity.created(url).body(convertToDTO(savedUsuario));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Correo electrónico ya está en uso.");
        }
    }




    @PutMapping("/{id}")
    public ResponseEntity<?> updateUsuario(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
        return usuarioRepository.findById(id)
                .map(existingUsuario -> {
                    updates.forEach((key, value) -> {
                        switch (key) {
                            case "nombre":
                                String nombre = (String) value;
                                if (nombre != null && !nombre.isBlank()) {
                                    existingUsuario.setNombre(nombre);
                                } else {
                                    throw new IllegalArgumentException("El nombre no puede estar en blanco");
                                }
                                break;
                            case "correoElectronico":
                                String correoElectronico = (String) value;
                                if (correoElectronico != null && correoElectronico.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                                    existingUsuario.setCorreoElectronico(correoElectronico);
                                } else {
                                    throw new IllegalArgumentException("El correo electrónico no es válido");
                                }
                                break;
                            case "contrasena":
                                String contrasena = (String) value;
                                if (contrasena != null && !contrasena.isBlank()) {
                                    existingUsuario.setContrasena(contrasena);
                                } else {
                                    throw new IllegalArgumentException("La contraseña no puede estar en blanco");
                                }
                                break;
                            default:
                                break;
                        }
                    });
                    try {
                        return ResponseEntity.ok(convertToDTO(usuarioRepository.save(existingUsuario)));
                    } catch (DataIntegrityViolationException e) {
                        return ResponseEntity.badRequest().body("El correo electrónico ya está en uso.");
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Integer id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            usuarioRepository.delete(usuario.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
