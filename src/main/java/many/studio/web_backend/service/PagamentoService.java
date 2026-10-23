package many.studio.web_backend.service;

import many.studio.web_backend.dto.ComprovanteResponse;
import many.studio.web_backend.dto.pagamento.PagamentoRequest;
import many.studio.web_backend.entity.*;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.security.SecureRandom;
import java.text.Normalizer;
import java.util.*;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final StatusAgendamentoRepository statusAgendamentoRepository;
    private final StatusPagamentoRepository statusPagamentoRepository;
    private final TipoPagamentoRepository tipoPagamentoRepository;
    private final ComprovanteRepository comprovanteRepository;

    public PagamentoService(PagamentoRepository pagamentoRepository, AgendamentoRepository agendamentoRepository,
                            StatusAgendamentoRepository statusAgendamentoRepository,
                            StatusPagamentoRepository statusPagamentoRepository, TipoPagamentoRepository tipoPagamentoRepository, ComprovanteRepository comprovanteRepository) {
        this.pagamentoRepository = pagamentoRepository;
        this.agendamentoRepository = agendamentoRepository;
        this.statusAgendamentoRepository = statusAgendamentoRepository;
        this.statusPagamentoRepository = statusPagamentoRepository;
        this.tipoPagamentoRepository = tipoPagamentoRepository;
        this.comprovanteRepository = comprovanteRepository;
    }

    public List<Pagamento> criarSinal(
            List<Long> idAgendamentos,
            MultipartFile comprovante) {

        List<Pagamento> pagamentos = new ArrayList<>();

        StatusAgendamento statusAgendamento =
                statusAgendamentoRepository
                        .findByEstado("solicitar confirmacao agendamento")
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Status de agendamento não encontrado"));

        StatusPagamento status =
                statusPagamentoRepository.findByEstado("pago");

        TipoPagamento tipoPagamento =
                tipoPagamentoRepository.findByTipo("sinal");

        String extensao = obterExtensao(comprovante);

        try (S3Client s3Client = S3Client.builder()
                .region(Region.US_EAST_1)
                .build()) {

            for (Long id : idAgendamentos) {

                Agendamento agendamento =
                        agendamentoRepository.findById(id)
                                .orElseThrow(() ->
                                        new EntityNotFoundException(
                                                "Agendamento não encontrado: " + id));

                String nomeCliente =
                        agendamento.getCliente().getNome();

                String keyName = enviarComprovanteS3(
                        s3Client,
                        comprovante,
                        agendamento.getId(),
                        nomeCliente,
                        extensao
                );

                Pagamento pagamento = new Pagamento();

                pagamento.setValor(
                        agendamento.getPacote()
                                .getServico()
                                .getSinalValor()
                );

                pagamento.setAgendamento(agendamento);
                pagamento.setTipoPagamento(tipoPagamento);
                pagamento.setStatusPagamento(status);

                pagamento.setComprovanteUrl(keyName);

                Pagamento saved = pagamentoRepository.save(pagamento);

                agendamento.setStatusAgendamento(statusAgendamento);
                agendamentoRepository.save(agendamento);

                pagamentos.add(saved);
            }

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Erro ao ler o comprovante.", e);
        }

        return pagamentos;
    }

    private String enviarComprovanteS3(
            S3Client s3Client,
            MultipartFile comprovante,
            Long idAgendamento,
            String nomeCliente,
            String extensao) throws IOException {

        String bucketName = "studio-essencia-comprovantes2";

        String nomeArquivo = gerarNomeComprovante(
                idAgendamento,
                nomeCliente,
                extensao
        );

        String keyName = "comprovantes/" + nomeArquivo;

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .contentType(comprovante.getContentType())
                .build();

        s3Client.putObject(
                request,
                RequestBody.fromBytes(comprovante.getBytes())
        );

        return keyName;
    }

    public ComprovanteResponse buscarComprovantePorAgendamento(
            Long agendamentoId) {

        Pagamento pagamento = pagamentoRepository
                .findByAgendamentoId(agendamentoId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Comprovante não encontrado"
                ));

        String keyName = pagamento.getComprovanteUrl();

        if (keyName == null || keyName.isBlank()) {
            throw new EntityNotFoundException(
                    "Comprovante não encontrado no pagamento"
            );
        }

        try (S3Client s3Client = S3Client.builder()
                .region(Region.US_EAST_1)
                .build()) {

            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket("studio-essencia-comprovantes2")
                    .key(keyName)
                    .build();

            ResponseBytes<GetObjectResponse> arquivo =
                    s3Client.getObjectAsBytes(request);

            String arquivoBase64 = Base64.getEncoder()
                    .encodeToString(arquivo.asByteArray());

            String tipoArquivo = arquivo.response().contentType();

            return new ComprovanteResponse(
                    arquivoBase64,
                    tipoArquivo
            );

        }
    }

    private String gerarNomeComprovante(
            Long idAgendamento,
            String nomeCliente,
            String extensao) {

        String nomeNormalizado = Normalizer
                .normalize(nomeCliente, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");

        int numeroAleatorio = new SecureRandom().nextInt(10_000);

        return idAgendamento + "_" +
                nomeNormalizado + "_" +
                String.format("%04d", numeroAleatorio) +
                "." + extensao;
    }

    private String obterExtensao(MultipartFile arquivo) {
        String contentType = arquivo.getContentType();

        if ("image/png".equalsIgnoreCase(contentType)) {
            return "png";
        }

        if ("image/jpeg".equalsIgnoreCase(contentType)) {
            return "jpeg";
        }

        if ("application/pdf".equalsIgnoreCase(contentType)) {
            return "pdf";
        }

        throw new IllegalArgumentException(
                "Formato inválido. Envie PNG, JPEG ou PDF."
        );
    }
}
