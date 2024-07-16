package com.NicoValdes.ForoHubChallenge.controller;
import com.NicoValdes.ForoHubChallenge.DTOs.TopicoDTO;
import com.NicoValdes.ForoHubChallenge.models.Curso;
import com.NicoValdes.ForoHubChallenge.models.Usuario;
import com.NicoValdes.ForoHubChallenge.repository.TopicoRepository;
import com.NicoValdes.ForoHubChallenge.repository.CursoRepository;
import com.NicoValdes.ForoHubChallenge.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.NicoValdes.ForoHubChallenge.models.Topico;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    private TopicoDTO convertToDTO(Topico topico) {
        return new TopicoDTO(

                topico.getTitulo(),
                topico.getMensaje(),
                topico.getStatus(),
                topico.getAutor().getId(),
                topico.getCurso().getId()
        );
    }

    private Topico convertToEntity(TopicoDTO topicoDTO) {
        Topico topico = new Topico();
        topico.setTitulo(topicoDTO.titulo());
        topico.setMensaje(topicoDTO.mensaje());
        topico.setFechaCreacion(LocalDateTime.now()); // Set current date and time
        topico.setStatus(topicoDTO.status());

        Usuario autor = usuarioRepository.findById(topicoDTO.autorId())
                .orElseThrow(() -> new IllegalArgumentException("Autor no encontrado"));
        topico.setAutor(autor);

        Curso curso = cursoRepository.findById(topicoDTO.cursoId())
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));
        topico.setCurso(curso);

        return topico;
    }


    @GetMapping
    public List<Topico> getAllTopicos() {
        return topicoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Topico> getTopicoById(@PathVariable Integer id) {
        return topicoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TopicoDTO> createTopico(@Valid @RequestBody TopicoDTO topicoDTO, UriComponentsBuilder uriBuilder) {
        Topico topico = convertToEntity(topicoDTO);
        Topico savedTopico = topicoRepository.save(topico);
        URI url = uriBuilder.path("/topicos/{id}").buildAndExpand(savedTopico.getId()).toUri();
        return ResponseEntity.created(url).body(convertToDTO(savedTopico));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTopico(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
        return topicoRepository.findById(id)
                .map(existingTopico -> {
                    updates.forEach((key, value) -> {
                        switch (key) {
                            case "titulo":
                                String titulo = (String) value;
                                if (titulo != null && !titulo.isBlank()) {
                                    existingTopico.setTitulo(titulo);
                                } else {
                                    throw new IllegalArgumentException("El título no puede estar en blanco");
                                }
                                break;
                            case "mensaje":
                                String mensaje = (String) value;
                                if (mensaje != null && !mensaje.isBlank()) {
                                    existingTopico.setMensaje(mensaje);
                                } else {
                                    throw new IllegalArgumentException("El mensaje no puede estar en blanco");
                                }
                                break;
                            case "status":
                                String status = (String) value;
                                existingTopico.setStatus(status);
                                break;
                            case "autorId":
                                Integer autorId = ((Number) value).intValue();
                                Usuario autor = usuarioRepository.findById(autorId)
                                        .orElseThrow(() -> new IllegalArgumentException("Autor no encontrado"));
                                existingTopico.setAutor(autor);
                                break;
                            case "cursoId":
                                Integer cursoId = ((Number) value).intValue();
                                Curso curso = cursoRepository.findById(cursoId)
                                        .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));
                                existingTopico.setCurso(curso);
                                break;
                            default:
                                break;
                        }
                    });
                    Topico updatedTopico = topicoRepository.save(existingTopico);
                    return ResponseEntity.ok(convertToDTO(updatedTopico));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTopico(@PathVariable Integer id) {
        Optional<Topico> topico = topicoRepository.findById(id);
        if (topico.isPresent()) {
            topicoRepository.delete(topico.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
