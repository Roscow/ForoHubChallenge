package com.NicoValdes.ForoHubChallenge.repository;
import com.NicoValdes.ForoHubChallenge.models.Respuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RespuestaRepository extends JpaRepository<Respuesta, Integer> {
    // Métodos de consulta personalizados si es necesario
}
