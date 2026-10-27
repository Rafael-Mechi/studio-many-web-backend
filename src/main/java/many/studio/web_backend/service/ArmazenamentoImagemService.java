package many.studio.web_backend.service;

import many.studio.web_backend.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class ArmazenamentoImagemService {

    private static final long TAMANHO_MAXIMO_BYTES = 2L * 1024 * 1024;

    private final S3Client s3Client;


    @Value("${aws.s3.bucket.imagens:${aws.s3.bucket.comprovantes}}")
    private String bucketImagens;

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.prefix.servicos:servicos/}")
    private String prefixServicos;

    @Value("${aws.s3.prefix.usuarios:usuarios/}")
    private String prefixUsuarios;

    public ArmazenamentoImagemService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String salvarImagemServico(MultipartFile arquivo) {
        return salvarImagem(prefixServicos, arquivo);
    }

    public String salvarImagemUsuario(MultipartFile arquivo) {
        return salvarImagem(prefixUsuarios, arquivo);
    }

    private String salvarImagem(String prefix, MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new BadRequestException("Selecione uma imagem");
        }

        if (arquivo.getSize() > TAMANHO_MAXIMO_BYTES) {
            throw new BadRequestException("A imagem deve ter no máximo 2 MB");
        }

        try {
            byte[] conteudo = arquivo.getBytes();
            String extensao = detectarExtensao(conteudo);

            String keyName = normalizarPrefixo(prefix) + UUID.randomUUID() + "." + extensao;

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketImagens)
                    .key(keyName)
                    .contentType(contentTypePorExtensao(extensao))
                    .build();

            s3Client.putObject(request, RequestBody.fromBytes(conteudo));

            return montarUrl(keyName);
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao ler a imagem", e);
        }
    }

    private String montarUrl(String keyName) {
        return "https://" + bucketImagens + ".s3." + region + ".amazonaws.com/" + keyName;
    }

    private String normalizarPrefixo(String prefix) {
        if (prefix == null || prefix.isBlank()) {
            return "";
        }
        return prefix.endsWith("/") ? prefix : prefix + "/";
    }

    private String contentTypePorExtensao(String extensao) {
        return "png".equals(extensao) ? "image/png" : "image/jpeg";
    }

    private String detectarExtensao(byte[] b) {
        boolean jpeg = b.length >= 3
                && (b[0] & 0xFF) == 0xFF && (b[1] & 0xFF) == 0xD8 && (b[2] & 0xFF) == 0xFF;
        boolean png = b.length >= 8
                && (b[0] & 0xFF) == 0x89 && b[1] == 0x50 && b[2] == 0x4E && b[3] == 0x47;

        if (jpeg) {
            return "jpg";
        }
        if (png) {
            return "png";
        }
        throw new BadRequestException("Formato inválido: envie uma imagem JPG ou PNG");
    }
}