package many.studio.web_backend.repository;

import jakarta.transaction.Transactional;
import many.studio.web_backend.entity.AgendamentoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import java.util.List;

public interface AgendamentoItemRepository extends JpaRepository<AgendamentoItem, Long> {
    @Transactional
    void deleteByAgendamentoId(Long agendamentoId);

    @Query("""
            SELECT ai FROM AgendamentoItem ai
            JOIN ai.agendamento a
            WHERE a.profissional.id = :profissionalId
            """)
    List<AgendamentoItem> findByProfissionalId(@Param("profissionalId") Long profissionalId);


    @Query("""
            SELECT ai FROM AgendamentoItem  ai
            JOIN ai.agendamento a
             WHERE a.cliente.id = :clienteId
    """)
    List<AgendamentoItem> findByClienteId(@Param("clienteId") Long clienteId);

    List<AgendamentoItem> findByAgendamentoId(@Param("agendamento_id") Long agendamentoId);

    List<AgendamentoItem> findByAgendamentoId(Long agendamentoId);
}
