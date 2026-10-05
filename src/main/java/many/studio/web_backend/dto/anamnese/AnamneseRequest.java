package many.studio.web_backend.dto.anamnese;

import many.studio.web_backend.entity.Cliente;
import many.studio.web_backend.entity.Profissional;

public class AnamneseRequest {
    // private Long profissional; <- id do profissional (na vdd do usuario*) já é passado no token
    private Long cliente;
    private String informacao;

    public AnamneseRequest(){}

    public AnamneseRequest(Long profissional, Long cliente, String informacao) {
        this.cliente = cliente;
        this.informacao = informacao;
    }

    public Long getCliente() {
        return cliente;
    }

    public void setCliente(Long cliente) {
        this.cliente = cliente;
    }

    public String getInformacao() {
        return informacao;
    }

    public void setInformacao(String informacao) {
        this.informacao = informacao;
    }
}
