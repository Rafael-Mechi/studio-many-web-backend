package many.studio.web_backend.repository;

import many.studio.web_backend.entity.Comprovante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComprovanteRepository extends JpaRepository<Comprovante, Long> {
    Optional<Comprovante> findByPagamentoAgendamentoId(Long agendamentoId);
}
