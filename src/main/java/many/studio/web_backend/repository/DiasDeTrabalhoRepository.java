package many.studio.web_backend.repository;

import many.studio.web_backend.entity.DiasDeTrabalho;
import many.studio.web_backend.entity.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiasDeTrabalhoRepository extends JpaRepository<DiasDeTrabalho, Long> {

    List<DiasDeTrabalho> findByProfissionalId(Long id);
}
