package com.NicoValdes.ForoHubChallenge.repository;
import com.NicoValdes.ForoHubChallenge.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    UserDetails findByCorreoElectronico(String email);
    // Puedes agregar métodos de consulta personalizados aquí si es necesario
}
