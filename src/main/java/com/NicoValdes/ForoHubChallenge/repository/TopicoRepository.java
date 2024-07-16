package com.NicoValdes.ForoHubChallenge.repository;
import com.NicoValdes.ForoHubChallenge.models.Topico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicoRepository extends JpaRepository<Topico, Integer> {
    // Métodos de consulta personalizados si es necesario
}

