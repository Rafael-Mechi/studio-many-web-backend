package many.studio.web_backend.repository;

import many.studio.web_backend.entity.OtpCodigo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OtpCodigoRepository extends JpaRepository<OtpCodigo, Long> {

    List<OtpCodigo> findByDestinoAndUsadoFalse(String destino);

    Optional<OtpCodigo> findFirstByDestinoAndUsadoFalseOrderByCriadoEmDesc(String destino);

    Optional<OtpCodigo> findFirstByResetTokenAndUsadoFalseAndVerificadoTrueAndResetTokenExpiraEmAfter(
            String resetToken, LocalDateTime agora);
}
