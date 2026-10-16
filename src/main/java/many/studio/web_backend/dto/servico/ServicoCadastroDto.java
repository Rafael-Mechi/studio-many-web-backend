package many.studio.web_backend.dto.servico;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.List;

public class ServicoCadastroDto {

    @NotBlank
    @Size(min = 1, max = 45)
    @Schema(description = "Nome do serviço", example = "Limpeza de pele")
    private String nome;

    @NotBlank
    @Size(min = 1, max = 255)
    @Schema(description = "Descrição do serviço", example = "procedimento de remover impurezas, células mortas, cravos e miliuns da superfície do rosto.")
    private String descricao;

    @Size(max = 255)
    @Schema(description = "Url para a imagen do serviço (opcional)", example = "https://[NOME_DO_BUCKET].s3.[REGIÃO]://[CAMINHO/DA/IMAGEM.jpg]")
    private String fotoUrl;

    @NotNull
    @Positive
    @Schema(description = "Tempo que a execução do serviço dura.", example = "40")
    private Integer duracaoMinutos;

    @NotNull
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "50000.00")
    @Schema(description = "Custo do serviço", example = "120.00")
    private Double preco;

    @NotNull
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "500.00")
    @Schema(description = "Custo do sinal para aquele serviço", example = "50.00")
    private Double sinalValor;

    @NotNull
    @Schema(description = "Disponibilidade do serviço", example = "true")
    private Boolean ativo;

    @NotNull
    @Schema(description = "Id da categoria do serviço (GET /servicos/categorias)", example = "1")
    private Long categoriaId;

    @Schema(description = "Ids dos profissionais habilitados a realizar o serviço", example = "[1, 2]")
    private List<Long> profissionaisIds;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public Integer getDuracaoMinutos() {
        return duracaoMinutos;
    }

    public void setDuracaoMinutos(Integer duracaoMinutos) {
        this.duracaoMinutos = duracaoMinutos;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Double getSinalValor() {
        return sinalValor;
    }

    public void setSinalValor(Double sinalValor) {
        this.sinalValor = sinalValor;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public List<Long> getProfissionaisIds() {
        return profissionaisIds;
    }

    public void setProfissionaisIds(List<Long> profissionaisIds) {
        this.profissionaisIds = profissionaisIds;
    }
}
