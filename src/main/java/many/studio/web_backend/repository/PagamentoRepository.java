package many.studio.web_backend.repository;

import many.studio.web_backend.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    @Query("""
    SELECT DISTINCT pg
    FROM Pagamento pg
    JOIN pg.agendamento a
    JOIN a.itens ai
    WHERE a.cliente.id = :clienteId
      AND pg.statusPagamento.estado = :estado
      AND (:usuarioId IS NULL OR ai.profissional.usuario.id = :usuarioId)
""")
    List<Pagamento> findByAgendamentoClienteIdAndStatusPagamentoEstado(
            @Param("clienteId") Long clienteId,
            @Param("estado") String estado,
            @Param("usuarioId") Long usuarioId
    );
}
