package many.studio.web_backend.dto.profissional;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ProfissionalResponseDto(
        @Schema(description = "ID do profissional", example = "1")
        Long id,

        @NotBlank
        @Schema(description = "Nome atualizado do profissional", example = "Carlos Souza")
        String nome,

        @NotBlank
        @Email
        @Schema(description = "E-mail atualizado de acesso", example = "carlos.souza@email.com")
        String email
) {
}