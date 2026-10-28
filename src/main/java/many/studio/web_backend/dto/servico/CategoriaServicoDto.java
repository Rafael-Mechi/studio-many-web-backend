package many.studio.web_backend.dto.servico;

import io.swagger.v3.oas.annotations.media.Schema;

public class CategoriaServicoDto {

    @Schema(description = "Id da categoria", example = "1")
    private Long id;

    @Schema(description = "Nome da categoria", example = "Facial")
    private String categoria;

    public CategoriaServicoDto() {}

    public CategoriaServicoDto(Long id, String categoria) {
        this.id = id;
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
