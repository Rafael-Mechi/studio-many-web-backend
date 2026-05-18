package many.studio.web_backend.repository;

import many.studio.web_backend.entity.Pacote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PacoteRepository extends JpaRepository<Pacote, Long> {

    List<Pacote> findByServicoId(Long id);

}
