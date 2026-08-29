package many.studio.web_backend.repository;

import many.studio.web_backend.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    List<Pagamento> findByAgendamentoClienteIdAndStatusPagamentoEstado(Long clienteId, String pago);
}
