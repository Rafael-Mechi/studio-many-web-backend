package many.studio.web_backend.repository;

import many.studio.web_backend.entity.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
    Optional<Profissional> findByUsuario_Id(Long usuarioId);
}
