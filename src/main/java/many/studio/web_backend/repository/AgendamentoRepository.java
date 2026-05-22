package many.studio.web_backend.repository;

import many.studio.web_backend.dto.profissional.AgendamentoHistoricoDto;
import many.studio.web_backend.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    @Query("SELECT s.nome FROM AgendamentoItem ai " +
            "JOIN ai.agendamento a " +
            "JOIN ai.servico s " +
            "WHERE a.cliente.id = :clienteId " +
            "GROUP BY s.nome " +
            "ORDER BY COUNT(ai.id) DESC")
    List<String> findServicoPreferidoByClienteId(@Param("clienteId") Long clienteId);

    @Query("SELECT new many.studio.web_backend.dto.profissional.AgendamentoHistoricoDto(" +
            "  s.nome, " +
            "  a.inicio, " +
            "  p.nome, " +
            "  sa.estado" +
            ") " +
            "FROM AgendamentoItem ai " +
            "JOIN ai.agendamento a " +
            "JOIN ai.servico s " +
            "JOIN ai.profissional p " +
            "JOIN a.statusAgendamento sa " +
            "WHERE a.cliente.id = :clienteId " +
            "ORDER BY a.inicio DESC")
    List<AgendamentoHistoricoDto> findHistoricoRecenteByClienteId(@Param("clienteId") Long clienteId);
}
