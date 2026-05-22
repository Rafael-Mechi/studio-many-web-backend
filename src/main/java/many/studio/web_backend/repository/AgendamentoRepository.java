package many.studio.web_backend.repository;

import many.studio.web_backend.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    @Query("SELECT MAX(FUNCTION('DATE', a.criadoEm)) " +
            "FROM Agendamento a " +
            "WHERE a.cliente.id = :clienteId")
    LocalDate findUltimaVisitaByClienteId(@Param("clienteId") Long clienteId);

    @Query("SELECT SUM(ai.precoFinal) FROM AgendamentoItem ai " +
            "JOIN ai.agendamento a " +
            "JOIN a.statusAgendamento sa " +
            "WHERE a.cliente.id = :clienteId " +
            "AND sa.estado = 'PAGO'")
    Double findTotalGastoByClienteId(@Param("clienteId") Long clienteId);

    @Query("SELECT s.nome FROM AgendamentoItem ai " +
            "JOIN ai.agendamento a " +
            "JOIN ai.servico s " +
            "WHERE a.cliente.id = :clienteId " +
            "GROUP BY s.nome " +
            "ORDER BY COUNT(ai.id) DESC")
    List<String> findServicoPreferidoByClienteId(@Param("clienteId") Long clienteId);
}
