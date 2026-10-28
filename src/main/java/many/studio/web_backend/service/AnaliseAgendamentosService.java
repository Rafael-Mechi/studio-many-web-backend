package many.studio.web_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import many.studio.web_backend.dto.agendamento.AgendamentoAnaliseResponse;
import many.studio.web_backend.dto.agendamento.ProbabilidadeNoShowResponse;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.exception.ForbiddenException;
import many.studio.web_backend.repository.AgendamentoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public class AnaliseAgendamentosService {
    private final AgendamentoRepository agendamentoRepository;
    private final S3Client s3Client;

    @Value("${aws.s3.bucket.comprovantes}")
    private String bucket;

    @Value("${aws.s3.prefix.comprovantes}")
    private String prefix;

    public AnaliseAgendamentosService(
            AgendamentoRepository agendamentoRepository,
            S3Client s3Client) {
        this.agendamentoRepository = agendamentoRepository;
        this.s3Client = s3Client;
    }

    @Transactional(readOnly = true)
    public List<AgendamentoAnaliseResponse> buscarPorData(
            LocalDate data) {

        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = data.plusDays(1).atStartOfDay();

        return agendamentoRepository.buscarDadosParaAnalise(
                inicio,
                fim
        );
    }

    public ProbabilidadeNoShowResponse buscarProbabilidadeNoShow(
            Long idAgendamento,
            LocalDate dataAgendamento,
            String role) {

        if (!"ROLE_ADMIN".equals(role) && !"ROLE_PROFISSIONAL".equals(role)) {
            throw new ForbiddenException("Você não tem permissão para acessar esse recurso");
        }

        String key = prefix + dataAgendamento + ".csv";
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        try (ResponseInputStream<GetObjectResponse> arquivo = s3Client.getObject(request);
             BufferedReader leitor = new BufferedReader(
                     new InputStreamReader(arquivo, StandardCharsets.UTF_8))) {

            Map<String, Integer> colunas = obterColunas(leitor.readLine());
            Integer indiceId = colunas.get("id_agendamento");
            Integer indiceProbabilidade = colunas.get("probabilidade_falta");

            if (indiceId == null || indiceProbabilidade == null) {
                throw new IllegalStateException("O CSV de probabilidade não possui as colunas esperadas");
            }

            String linha;
            while ((linha = leitor.readLine()) != null) {
                List<String> valores = separarCsv(linha);
                if (valores.size() <= Math.max(indiceId, indiceProbabilidade)) {
                    continue;
                }

                if (idAgendamento.equals(Long.valueOf(valores.get(indiceId).trim()))) {
                    return new ProbabilidadeNoShowResponse(
                            idAgendamento,
                            new BigDecimal(valores.get(indiceProbabilidade).trim())
                    );
                }
            }
        } catch (S3Exception ex) {
            if (ex.statusCode() == 404) {
                throw new EntityNotFoundException("Arquivo de probabilidade não encontrado para a data informada");
            }
            throw ex;
        } catch (java.io.IOException ex) {
            throw new IllegalStateException("Não foi possível ler o CSV de probabilidade", ex);
        }

        throw new EntityNotFoundException("Agendamento não encontrado no CSV de probabilidade");
    }

    private Map<String, Integer> obterColunas(String cabecalho) {
        if (cabecalho == null) {
            throw new IllegalStateException("O CSV de probabilidade está vazio");
        }

        Map<String, Integer> colunas = new HashMap<>();
        List<String> valores = separarCsv(cabecalho);
        for (int i = 0; i < valores.size(); i++) {
            colunas.put(valores.get(i).trim(), i);
        }
        return colunas;
    }

    private List<String> separarCsv(String linha) {
        return java.util.Arrays.asList(linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1));
    }
}
