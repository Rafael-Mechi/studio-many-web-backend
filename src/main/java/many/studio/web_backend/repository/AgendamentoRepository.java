package many.studio.web_backend.repository;

import many.studio.web_backend.dto.profissional.AgendamentoHistoricoDto;
import many.studio.web_backend.dto.agendamento.HorarioIndisponivelDto;
import many.studio.web_backend.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    @Query("""
        SELECT s.nome FROM Agendamento a
        JOIN a.pacote pac
        JOIN pac.servico s
        WHERE a.cliente.id = :clienteId
        GROUP BY s.nome
        ORDER BY COUNT(a.id) DESC
    """)
    List<String> findServicoPreferidoByClienteId(@Param("clienteId") Long clienteId);

    @Query("""
        SELECT new many.studio.web_backend.dto.profissional.AgendamentoHistoricoDto(
          s.nome,
          ai.inicioAtendimento,
          p.nome,
          sa.estado
        )
        FROM AgendamentoItem ai\s
        JOIN ai.agendamento a
        JOIN a.pacote pac
        JOIN pac.servico s
        JOIN a.profissional p
        JOIN a.statusAgendamento sa
        WHERE a.cliente.id = :clienteId
        ORDER BY ai.inicioAtendimento DESC
    """)
    List<AgendamentoHistoricoDto> findHistoricoRecenteByClienteId(@Param("clienteId") Long clienteId);

    @Query("""
        SELECT new many.studio.web_backend.dto.agendamento.HorarioIndisponivelDto(
            ai.inicioAtendimento,
            ai.fimAtendimento,
            p.nome,
            s.nome
        )
        FROM AgendamentoItem ai\s
        JOIN ai.agendamento a
        JOIN a.profissional p
        JOIN a.pacote pac
        JOIN pac.servico s
        JOIN a.statusAgendamento sa
        WHERE s.nome = :nomeServico
        AND sa.estado <> 'cancelado'
    """)
    List<HorarioIndisponivelDto> buscarAgendamentosIndisponiveis(String nomeServico);

    @Query("""
        SELECT new many.studio.web_backend.dto.agendamento.HorarioIndisponivelDto(
            b.inicio,
            b.fim,
            p.nome,
            'BLOQUEIO'
        )
        FROM Bloqueio b
        JOIN b.profissional p
        WHERE EXISTS (
            SELECT sp.id
            FROM ServicoProfissional sp
            WHERE sp.profissional.id = p.id
            AND sp.servico.nome = :nomeServico
        )
    """)
    List<HorarioIndisponivelDto> buscarBloqueios(String nomeServico);
}
