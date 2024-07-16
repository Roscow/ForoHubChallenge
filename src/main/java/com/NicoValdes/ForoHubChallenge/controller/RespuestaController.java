package com.NicoValdes.ForoHubChallenge.controller;
import com.NicoValdes.ForoHubChallenge.DTOs.RespuestaDTO;
import com.NicoValdes.ForoHubChallenge.models.Topico;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.NicoValdes.ForoHubChallenge.repository.RespuestaRepository;
import com.NicoValdes.ForoHubChallenge.repository.TopicoRepository;
import com.NicoValdes.ForoHubChallenge.models.Respuesta;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/respuestas")
public class RespuestaController {

    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    private RespuestaDTO convertToDTO(Respuesta respuesta) {
        return new RespuestaDTO(
                respuesta.getMensaje(),
                respuesta.getSolucion(),
                respuesta.getTopico().getId()
        );
    }

    private Respuesta convertToEntity(RespuestaDTO respuestaDTO) {
        Respuesta respuesta = new Respuesta();
        respuesta.setMensaje(respuestaDTO.mensaje());
        respuesta.setSolucion(respuestaDTO.solucion());

        Topico topico = topicoRepository.findById(respuestaDTO.topicoId())
                .orElseThrow(() -> new IllegalArgumentException("Tópico no encontrado"));
        respuesta.setTopico(topico);
        return respuesta;
    }



    @GetMapping
    public List<Respuesta> getAllRespuestas() {
        return respuestaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Respuesta> getRespuestaById(@PathVariable Integer id) {
        return respuestaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<RespuestaDTO> createRespuesta(@Valid @RequestBody RespuestaDTO respuestaDTO, UriComponentsBuilder uriBuilder) {
        Respuesta respuesta = convertToEntity(respuestaDTO);
        Respuesta savedRespuesta = respuestaRepository.save(respuesta);
        URI url = uriBuilder.path("/respuestas/{id}").buildAndExpand(savedRespuesta.getId()).toUri();
        return ResponseEntity.created(url).body(convertToDTO(savedRespuesta));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateRespuesta(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
        return respuestaRepository.findById(id)
                .map(existingRespuesta -> {
                    updates.forEach((key, value) -> {
                        switch (key) {
                            case "mensaje":
                                String mensaje = (String) value;
                                if (mensaje != null && !mensaje.isBlank()) {
                                    existingRespuesta.setMensaje(mensaje);
                                } else {
                                    throw new IllegalArgumentException("El mensaje no puede estar en blanco");
                                }
                                break;
                            case "solucion":
                                Boolean solucion = (Boolean) value;
                                if (solucion != null) {
                                    existingRespuesta.setSolucion(solucion);
                                } else {
                                    throw new IllegalArgumentException("El campo solución no puede estar en blanco");
                                }
                                break;
                            case "topicoId":
                                Integer topicoId = ((Number) value).intValue();
                                Topico topico = topicoRepository.findById(topicoId)
                                        .orElseThrow(() -> new IllegalArgumentException("Tópico no encontrado"));
                                existingRespuesta.setTopico(topico);
                                break;
                            default:
                                break;
                        }
                    });
                    Respuesta updatedRespuesta = respuestaRepository.save(existingRespuesta);
                    return ResponseEntity.ok(convertToDTO(updatedRespuesta));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRespuesta(@PathVariable Integer id) {
        Optional<Respuesta> respuesta = respuestaRepository.findById(id);
        if (respuesta.isPresent()) {
            respuestaRepository.delete(respuesta.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
