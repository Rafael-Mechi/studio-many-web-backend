package many.studio.web_backend.repository;

import many.studio.web_backend.entity.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusAgendamentoRepository extends JpaRepository<StatusAgendamento, Long> {

    Optional<StatusAgendamento> findStatusAgendamentoByEstado(String estado);
}