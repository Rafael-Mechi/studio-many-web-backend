package many.studio.web_backend.dto.anamnese;

public class AnamneseListResponse {
    private Long id;
    private String nomeCliente;
    private String telefoneCliente;
    private String informacao;

    public AnamneseListResponse() {}

    public AnamneseListResponse(Long id, String nomeCliente, String telefoneCliente, String informacao) {
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.telefoneCliente = telefoneCliente;
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

    public String getTelefoneCliente() {
        return telefoneCliente;
    }

    public void setTelefoneCliente(String telefoneCliente) {
        this.telefoneCliente = telefoneCliente;
    }

    public String getInformacao() {
        return informacao;
    }

    public void setInformacao(String informacao) {
        this.informacao = informacao;
    }
}
