package many.studio.web_backend.dto.servico;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public class ServicoListarDto {

    @Schema(description = "Id do serviço", example = "1")
    private Long id;

    @Schema(description = "Nome do serviço", example = "Limpeza de pele")
    private String nome;

    @Schema(description = "Descrição do serviço", example = "procedimento de remover impurezas, células mortas, cravos e miliuns da superfície do rosto.")
    private String descricao;

    @Schema(description = "Url para a imagen do serviço", example = "https://[NOME_DO_BUCKET].s3.[REGIÃO]://[CAMINHO/DA/IMAGEM.jpg]")
    private String fotoUrl;

    @Schema(description = "Tempo que a execução do serviço dura.", example = "40")
    private Integer duracaoMinutos;

    @Schema(description = "Custo do serviço", example = "120,00")
    private Double preco;

    @Schema(description = "Custo do sinal para aquele serviço", example = "50,00")
    private Double sinalValor;

    @Schema(description = "Disponibilidade do serviço", example = "true")
    private Boolean ativo;

    @Schema(description = "Data de criação do serviço", example = "15/09/2026")
    private LocalDateTime criadoEm;

    @Schema(description = "Categoria do serviço", example = "Facial")
    private String categoria;

    public ServicoListarDto(Long id, String nome, String descricao, String fotoUrl, Integer duracaoMinutos, Double preco, Double sinalValor, Boolean ativo, LocalDateTime criadoEm, String categoria) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.fotoUrl = fotoUrl;
        this.duracaoMinutos = duracaoMinutos;
        this.preco = preco;
        this.sinalValor = sinalValor;
        this.ativo = ativo;
        this.criadoEm = criadoEm;
        this.categoria = categoria;
    }

    public ServicoListarDto(){}

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getDuracaoMinutos() {
        return duracaoMinutos;
    }

    public void setDuracaoMinutos(Integer duracaoMinutos) {
        this.duracaoMinutos = duracaoMinutos;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}