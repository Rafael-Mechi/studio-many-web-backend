package many.studio.web_backend.repository;

import many.studio.web_backend.entity.Profissional;
import many.studio.web_backend.entity.ServicoProfissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicoProfissionalRepository extends JpaRepository<ServicoProfissional, Long> {

    List<ServicoProfissional> findAllByProfissionalId(Long profissionalId);

    List<Profissional> findByServicoId(Long id);
}
