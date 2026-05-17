package many.studio.web_backend.repository;

import many.studio.web_backend.entity.AgendamentoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendamentoItemRepository extends JpaRepository<AgendamentoItem, Long> {
}
