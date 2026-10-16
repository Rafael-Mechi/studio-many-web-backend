package many.studio.web_backend.dto.usuario.esqueci_senha;

import jakarta.validation.constraints.NotBlank;

public class VerificarOtpRequest {

    @NotBlank
    private String destino;

    @NotBlank
    private String codigo;

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
