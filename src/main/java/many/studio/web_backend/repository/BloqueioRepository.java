package many.studio.web_backend.repository;

import many.studio.web_backend.entity.Bloqueio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BloqueioRepository extends JpaRepository<Bloqueio, Long> {
    List<Bloqueio> findByProfissionalId(Long profissionalId);
}
