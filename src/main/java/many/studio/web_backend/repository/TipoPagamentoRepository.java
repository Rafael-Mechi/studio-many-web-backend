package many.studio.web_backend.repository;

import many.studio.web_backend.entity.TipoPagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoPagamentoRepository extends JpaRepository<TipoPagamento, Long> {

    TipoPagamento findByTipo(String tipo);
}
