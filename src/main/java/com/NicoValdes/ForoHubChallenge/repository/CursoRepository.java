package com.NicoValdes.ForoHubChallenge.repository;
import com.NicoValdes.ForoHubChallenge.models.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Integer> {
    // Métodos de consulta personalizados si es necesario
}
