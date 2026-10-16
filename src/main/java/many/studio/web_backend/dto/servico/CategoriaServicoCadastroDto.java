package many.studio.web_backend.dto.servico;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoriaServicoCadastroDto {

    @NotBlank
    @Size(min = 1, max = 45)
    @Schema(description = "Nome da categoria", example = "Capilar")
    private String categoria;

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
