package many.studio.web_backend.repository;

import many.studio.web_backend.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByUsuario_Id(Long usuarioId);
}
