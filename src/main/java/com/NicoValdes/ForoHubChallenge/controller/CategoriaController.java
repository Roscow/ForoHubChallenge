package com.NicoValdes.ForoHubChallenge.controller;
import com.NicoValdes.ForoHubChallenge.DTOs.CategoriaDTO;
import com.NicoValdes.ForoHubChallenge.models.Categoria;
import com.NicoValdes.ForoHubChallenge.repository.CategoriaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    private CategoriaDTO convertToDTO(Categoria categoria) {
        return new CategoriaDTO(
                categoria.getId(),
                categoria.getNombre()
        );
    }

    private Categoria convertToEntity(CategoriaDTO categoriaDTO) {
        Categoria categoria = new Categoria();
        categoria.setNombre(categoriaDTO.nombre());
        return categoria;
    }

    @GetMapping
    public List<Categoria> getAllCategorias() {
        return categoriaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getCategoriaById(@PathVariable Integer id) {
        return categoriaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CategoriaDTO> createCategoria(@Valid @RequestBody CategoriaDTO categoriaDTO, UriComponentsBuilder uriBuilder) {
        Categoria categoria = convertToEntity(categoriaDTO);
        Categoria savedCategoria = categoriaRepository.save(categoria);
        URI url = uriBuilder.path("/categorias/{id}").buildAndExpand(savedCategoria.getId()).toUri();
        return ResponseEntity.created(url).body(convertToDTO(savedCategoria));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> updateCategoria(@Valid @PathVariable Integer id,@Valid @RequestBody CategoriaDTO categoriaDetails) {
        return categoriaRepository.findById(id)
                .map(categoria -> {
                    categoria.setNombre(categoriaDetails.nombre());
                    return ResponseEntity.ok(convertToDTO(categoriaRepository.save(categoria)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Integer id) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        if (categoria.isPresent()) {
            categoriaRepository.delete(categoria.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
