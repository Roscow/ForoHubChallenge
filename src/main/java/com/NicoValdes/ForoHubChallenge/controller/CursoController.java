package com.NicoValdes.ForoHubChallenge.controller;
import com.NicoValdes.ForoHubChallenge.DTOs.CategoriaDTO;
import com.NicoValdes.ForoHubChallenge.DTOs.CursoDTO;
import com.NicoValdes.ForoHubChallenge.models.Categoria;
import com.NicoValdes.ForoHubChallenge.models.Curso;
import com.NicoValdes.ForoHubChallenge.repository.CategoriaRepository;
import com.NicoValdes.ForoHubChallenge.repository.CursoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private CursoDTO convertToDTO(Curso curso) {
        return new CursoDTO(
                curso.getNombre(),
                curso.getCategoria().getId()
        );
    }

    private Curso convertToEntity(CursoDTO cursoDTO) {
        Curso curso = new Curso();
        curso.setNombre(cursoDTO.nombre());
        Categoria categoria = categoriaRepository.findById(cursoDTO.categoriaId())
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
        curso.setCategoria(categoria);
        return curso;
    }

    @GetMapping
    public List<Curso> getAllCursos() {
        return cursoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> getCursoById(@PathVariable Integer id) {
        return cursoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CursoDTO> createCurso(@Valid @RequestBody CursoDTO cursoDTO , UriComponentsBuilder uriBuilder) {
        Curso curso = convertToEntity(cursoDTO);
        Curso savedCurso = cursoRepository.save(curso);
        URI url = uriBuilder.path("/cursos/{id}").buildAndExpand(savedCurso.getId()).toUri();
        return ResponseEntity.created(url).body(convertToDTO(curso));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursoDTO> updateCurso(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
        return cursoRepository.findById(id)
                .map(existingCurso -> {
                    updates.forEach((key, value) -> {
                        switch (key) {
                            case "nombre":
                                String nombre = (String) value;
                                if (nombre != null && !nombre.isBlank()) {
                                    existingCurso.setNombre(nombre);
                                } else {
                                    throw new IllegalArgumentException("El nombre no puede estar en blanco");
                                }
                                break;
                            case "categoriaId":
                                Integer categoriaId = ((Number) value).intValue();
                                Categoria categoria = categoriaRepository.findById(categoriaId)
                                        .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
                                existingCurso.setCategoria(categoria);
                                break;
                            default:
                                break;
                        }
                    });
                    return ResponseEntity.ok(convertToDTO(cursoRepository.save(existingCurso)));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurso(@PathVariable Integer id) {
        Optional<Curso> curso = cursoRepository.findById(id);
        if (curso.isPresent()) {
            cursoRepository.delete(curso.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
