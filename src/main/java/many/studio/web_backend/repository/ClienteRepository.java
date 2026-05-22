package many.studio.web_backend.repository;

import jakarta.websocket.server.PathParam;
import many.studio.web_backend.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByUsuario_Id(Long usuarioId);

    @Query("SELECT DISTINCT c " +
            "FROM Cliente c " +
            "JOIN Agendamento a ON a.cliente.id = c.id " +
            "JOIN AgendamentoItem ai ON ai.agendamento.id = a.id " +
            "WHERE ai.profissional.id = :profissionalId")
    List<Cliente> findClientesByProfissionalId(@Param("profissionalId") Long profissionalId);
}
