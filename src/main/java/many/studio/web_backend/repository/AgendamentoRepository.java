package many.studio.web_backend.repository;

import many.studio.web_backend.dto.agendamento.ResumoAgendamento;
import many.studio.web_backend.dto.profissional.AgendamentoHistoricoDto;
import many.studio.web_backend.dto.agendamento.HorarioIndisponivelDto;
import many.studio.web_backend.dto.usuario.HistoricoAgendamentos;
import many.studio.web_backend.entity.Agendamento;
import many.studio.web_backend.entity.AgendamentoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    List<Agendamento> findByProfissionalId(@Param("profissionalId") Long profissionalId);

    @Query("""
        SELECT DISTINCT a
        FROM Agendamento a
        JOIN a.itens ai
        WHERE a.profissional.id = :profissionalId
          AND ai.inicioAtendimento > CURRENT_TIMESTAMP
          AND a.statusAgendamento.estado NOT IN ('cancelado', 'concluido', 'recusado', 'faltou')
    """)
    List<Agendamento> findFuturosAtivosByProfissionalId(@Param("profissionalId") Long profissionalId);

    //    @Query("SELECT s.nome FROM AgendamentoItem ai " +
//        "JOIN ai.agendamento a " +
//        "JOIN ai.servico s " +
//        "WHERE a.cliente.id = :clienteId " +
//        "GROUP BY s.nome " +
//        "ORDER BY COUNT(ai.id) DESC"
//    )
//    List<String> findServicoPreferidoByClienteId(@Param("clienteId") Long clienteId);
//
//    @Query("SELECT new many.studio.web_backend.dto.profissional.AgendamentoHistoricoDto(" +
//        "  s.nome, " +
//        "  a.inicio, " +
//        "  p.nome, " +
//        "  sa.estado" +
//        ") " +
//        "FROM AgendamentoItem ai " +
//        "JOIN ai.agendamento a " +
//        "JOIN ai.servico s " +
//        "JOIN ai.profissional p " +
//        "JOIN a.statusAgendamento sa " +
//        "WHERE a.cliente.id = :clienteId " +
//        "ORDER BY a.inicio DESC")
//    List<AgendamentoHistoricoDto> findHistoricoRecenteByClienteId(@Param("clienteId") Long clienteId);
//
//    @Query("""
//        SELECT new many.studio.web_backend.dto.agendamento.HorarioIndisponivelDto(
//            a.inicio,
//            a.fim,
//            p.nome,
//            s.nome
//        )
//        FROM Agendamento a
//        JOIN AgendamentoItem ai ON ai.agendamento.id = a.id
//        JOIN ai.profissional p
//        JOIN ai.servico s
//        JOIN a.statusAgendamento sa
//        WHERE s.nome = :nomeServico
//        AND sa.estado <> 'cancelado'
//    """)
//    List<HorarioIndisponivelDto> buscarAgendamentosIndisponiveis(String nomeServico);
//
//    @Query("""
//        SELECT new many.studio.web_backend.dto.agendamento.HorarioIndisponivelDto(
//            b.inicio,
//            b.fim,
//            p.nome,
//            'BLOQUEIO'
//        )
//        FROM Bloqueio b
//        JOIN b.profissional p
//        WHERE EXISTS (
//            SELECT sp.id
//            FROM ServicoProfissional sp
//            WHERE sp.profissional.id = p.id
//            AND sp.servico.nome = :nomeServico
//        )
//    """)
//    List<HorarioIndisponivelDto> buscarBloqueios(String nomeServico);
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

    @Query("""
        SELECT a
        FROM Agendamento a
        JOIN a.profissional p
        JOIN a.cliente c
        WHERE p.usuario.id = :usuarioId
        OR c.usuario.id = :usuarioId
    """)
    List<Agendamento> findByUsuarioId(Long usuarioId);

    List<Agendamento> findByClienteUsuarioId(Long id);

    List<Agendamento> findByProfissionalUsuarioId(Long id);

    @Query("""
    SELECT DISTINCT a
    FROM Agendamento a
    JOIN a.itens ai
    WHERE a.cliente.id = :clienteId
      AND a.statusAgendamento.estado = :estado
      AND (:usuarioId IS NULL OR ai.profissional.usuario.id = :usuarioId)
""")
    List<Agendamento> findByClienteIdAndStatusAgendamentoEstado(
            @Param("clienteId") Long clienteId,
            @Param("estado") String estado,
            @Param("usuarioId") Long usuarioId
    );

    @Query("""
    SELECT new many.studio.web_backend.dto.agendamento.ResumoAgendamento(
        a.id,
        ai.inicioAtendimento,
        s.id, s.nome, s.preco, s.duracaoMinutos,
        cs.categoria,
        p.id, p.nome,
        prof.id, prof.usuario.email, prof.nome, prof.telefone,
        sa.estado
    )
    FROM Agendamento a
    JOIN AgendamentoItem ai ON ai.agendamento.id = a.id
    JOIN Servico s ON s.id = ai.servico.id
    JOIN CategoriaServico cs ON cs.id = s.categoriaServico.id
    JOIN Pacote p ON p.id = a.pacote.id
    JOIN Profissional prof ON prof.id = ai.profissional.id
    JOIN a.statusAgendamento sa
    WHERE a.cliente.id = :clienteId
      AND (:usuarioId IS NULL OR prof.usuario.id = :usuarioId)
    ORDER BY ai.inicioAtendimento DESC
""")
    List<ResumoAgendamento> buscarResumoCliente(
            @Param("clienteId") Long clienteId,
            @Param("usuarioId") Long usuarioId
    );

    @Query("""
    SELECT COUNT(DISTINCT a)
    FROM Agendamento a
    JOIN AgendamentoItem ai ON ai.agendamento.id = a.id
    WHERE a.cliente.id = :clienteId
      AND a.statusAgendamento.estado IN :status
      AND ai.inicioAtendimento >= CURRENT_TIMESTAMP
      AND (:usuarioId IS NULL OR ai.profissional.usuario.id = :usuarioId)
""")
    Long countAgendamentosPendentes(
            @Param("clienteId") Long clienteId,
            @Param("status") List<String> status,
            @Param("usuarioId") Long usuarioId
    );

    @Query("""
    SELECT new many.studio.web_backend.dto.usuario.HistoricoAgendamentos(
        s.nome,
        ai.inicioAtendimento,
        p.nome,
        sa.estado,
        s.preco
    )
    FROM Agendamento a
    JOIN a.itens ai
    JOIN ai.servico s
    JOIN ai.profissional p
    JOIN a.statusAgendamento sa
    WHERE a.cliente.id = :clienteId
      AND (:usuarioId IS NULL OR p.usuario.id = :usuarioId)
    ORDER BY ai.inicioAtendimento DESC
""")
    List<HistoricoAgendamentos> buscarHistoricoPorClienteEUsuarioProfissional(
            @Param("clienteId") Long clienteId,
            @Param("usuarioId") Long usuarioId
    );

    @Query("""
    SELECT COUNT(DISTINCT a.id)
    FROM Agendamento a
    JOIN a.statusAgendamento sa
    WHERE a.profissional.id = :profissionalId
      AND sa.estado = 'concluido'
""")
    Long countAgendamentosConcluidosPorProfissional(@Param("profissionalId") Long profissionalId);

    @Query("""
    SELECT DISTINCT cs.categoria
    FROM ServicoProfissional sp
    JOIN sp.servico s
    JOIN s.categoriaServico cs
    WHERE sp.profissional.id = :profissionalId
""")
    List<String> findCategoriasByProfissionalId(@Param("profissionalId") Long profissionalId);
}
