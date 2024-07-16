package com.NicoValdes.ForoHubChallenge.repository;
import com.NicoValdes.ForoHubChallenge.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    // Métodos de consulta personalizados si es necesario
}
