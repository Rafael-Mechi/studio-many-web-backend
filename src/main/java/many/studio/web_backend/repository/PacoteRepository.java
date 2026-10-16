package many.studio.web_backend.repository;

import many.studio.web_backend.entity.Pacote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PacoteRepository extends JpaRepository<Pacote, Long> {

    List<Pacote> findByServicoId(Long id);

    List<Pacote> findByServicoIdAndAtivoTrue(Long id);

    boolean existsByServicoIdAndNomeIgnoreCase(Long servicoId, String nome);

    boolean existsByServicoIdAndNomeIgnoreCaseAndIdNot(Long servicoId, String nome, Long id);
}
