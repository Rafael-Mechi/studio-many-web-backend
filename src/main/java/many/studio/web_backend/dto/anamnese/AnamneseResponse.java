package many.studio.web_backend.dto.anamnese;

public class AnamneseResponse {
    private Long id;
    private String nomeCliente;
    private String informacao;

    public AnamneseResponse(){}

    public AnamneseResponse(Long id, String nomeCliente, String informacao) {
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.informacao = informacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getInformacao() {
        return informacao;
    }

    public void setInformacao(String informacao) {
        this.informacao = informacao;
    }
}
