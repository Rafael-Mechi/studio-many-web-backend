package many.studio.web_backend.repository;

import many.studio.web_backend.entity.ServicoProfissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicoProfissionalRepository extends JpaRepository<ServicoProfissional, Long> {

}
