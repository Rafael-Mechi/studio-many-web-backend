package many.studio.web_backend.dto.pacote;

import io.swagger.v3.oas.annotations.media.Schema;


public class ServicoResumoDto {

    @Schema(description = "Id do serviço", example = "1")
    private Long id;

    @Schema(description = "Nome do serviço", example = "Limpeza de pele")
    private String nome;

    public ServicoResumoDto() {}

    public ServicoResumoDto(Long id, String nome) {
        this.id = id;
        this.nome = nome;
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
}
