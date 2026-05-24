package many.studio.web_backend.repository;

import many.studio.web_backend.dto.agendamento.HorarioIndisponivelDto;
import many.studio.web_backend.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    @Query("""
    SELECT new many.studio.web_backend.dto.agendamento.HorarioIndisponivelDto(
        a.inicio,
        a.fim,
        p.nome,
        s.nome
    )
    FROM Agendamento a
    JOIN AgendamentoItem ai ON ai.agendamento.id = a.id
    JOIN ai.profissional p
    JOIN ai.servico s
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
