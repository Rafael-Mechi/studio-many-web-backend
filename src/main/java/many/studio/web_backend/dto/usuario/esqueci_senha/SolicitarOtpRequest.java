package many.studio.web_backend.dto.usuario.esqueci_senha;

import jakarta.validation.constraints.NotBlank;

public class SolicitarOtpRequest {

    private String canal;

    @NotBlank
    private String destino;

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }
}
