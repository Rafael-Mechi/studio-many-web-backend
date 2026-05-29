package many.studio.web_backend.repository;

import jakarta.transaction.Transactional;
import many.studio.web_backend.entity.AgendamentoItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgendamentoItemRepository extends JpaRepository<AgendamentoItem, Long> {
    @Transactional
    void deleteByAgendamentoId(Long agendamentoId);

    List<AgendamentoItem> findByAgendamentoId(Long agendamentoId);
}
