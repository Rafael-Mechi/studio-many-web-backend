package many.studio.web_backend.repository;

import many.studio.web_backend.entity.StatusPagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusPagamentoRepository extends JpaRepository<StatusPagamento, Long> {

    StatusPagamento findByEstado(String estado);
}
