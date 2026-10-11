package many.studio.web_backend.repository;

import many.studio.web_backend.entity.AnamneseCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnamneseClienteRepository extends JpaRepository<AnamneseCliente, Long> {

    long countByClienteId(Long clienteId);
}
