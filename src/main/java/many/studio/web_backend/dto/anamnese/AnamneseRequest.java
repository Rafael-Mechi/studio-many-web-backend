package many.studio.web_backend.dto.anamnese;

public class AnamneseRequest {
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
