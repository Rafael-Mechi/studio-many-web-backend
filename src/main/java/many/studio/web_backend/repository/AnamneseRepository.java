package many.studio.web_backend.repository;

import many.studio.web_backend.entity.Anamnese;
import many.studio.web_backend.entity.Profissional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnamneseRepository extends JpaRepository<Anamnese, Long> {
    Page<Anamnese> findByProfissional(Profissional profissional, Pageable pageable);
}
