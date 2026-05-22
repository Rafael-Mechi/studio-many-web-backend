package many.studio.web_backend.repository;

import many.studio.web_backend.entity.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusAgendamentoRepository extends JpaRepository<StatusAgendamento, Long> {
}