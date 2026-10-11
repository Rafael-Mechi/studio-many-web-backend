package many.studio.web_backend.dto;

public class ComprovanteResponse {
    private String arquivo;
    private String tipoArquivo;

    public ComprovanteResponse() {
    }

    public ComprovanteResponse(String arquivo, String tipoArquivo) {
        this.arquivo = arquivo;
        this.tipoArquivo = tipoArquivo;
    }

    public String getArquivo() {
        return arquivo;
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

    public String getTipoArquivo() {
        return tipoArquivo;
    }

    public void setTipoArquivo(String tipoArquivo) {
        this.tipoArquivo = tipoArquivo;
    }
}
