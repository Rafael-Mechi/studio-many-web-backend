package many.studio.web_backend.repository;

import many.studio.web_backend.entity.CategoriaServico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaServicoRepository extends JpaRepository<CategoriaServico, Long> {

    boolean existsByCategoriaIgnoreCase(String categoria);

    Optional<CategoriaServico> findByCategoriaIgnoreCase(String categoria);
}
