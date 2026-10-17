package many.studio.web_backend.service;

import many.studio.web_backend.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ArmazenamentoImagemService {

    private static final long TAMANHO_MAXIMO_BYTES = 2L * 1024 * 1024;

    private final Path raiz;
    private final Path pastaServicos;
    private final Path pastaUsuarios;

    public ArmazenamentoImagemService(@Value("${app.upload-dir:uploads}") String diretorio) {
        this.raiz = Paths.get(diretorio).toAbsolutePath().normalize();
        this.pastaServicos = raiz.resolve("servicos");
        this.pastaUsuarios = raiz.resolve("usuarios");

        try {
            Files.createDirectories(pastaServicos);
            Files.createDirectories(pastaUsuarios);
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível criar a pasta de uploads: " + raiz, e);
        }
    }

    public Path getRaiz() {
        return raiz;
    }

    public String salvarImagemServico(MultipartFile arquivo) {
        return salvarImagem(pastaServicos, arquivo);
    }

    public String salvarImagemUsuario(MultipartFile arquivo) {
        return salvarImagem(pastaUsuarios, arquivo);
    }

    private String salvarImagem(Path pasta, MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new BadRequestException("Selecione uma imagem");
        }

        if (arquivo.getSize() > TAMANHO_MAXIMO_BYTES) {
            throw new BadRequestException("A imagem deve ter no máximo 2 MB");
        }

        try (InputStream entrada = arquivo.getInputStream()) {
            byte[] inicio = entrada.readNBytes(8);
            String extensao = detectarExtensao(inicio);

            String nomeArquivo = UUID.randomUUID() + "." + extensao;
            Path destino = pasta.resolve(nomeArquivo).normalize();

            try (InputStream completa = arquivo.getInputStream()) {
                Files.copy(completa, destino, StandardCopyOption.REPLACE_EXISTING);
            }

            return nomeArquivo;
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao salvar a imagem", e);
        }
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
