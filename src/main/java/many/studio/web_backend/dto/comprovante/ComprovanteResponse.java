package many.studio.web_backend.dto.comprovante;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;

public class ComprovanteResponse {
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String pdf;

    public ComprovanteResponse(String pdf) {
        this.pdf = pdf;
    }

    public ComprovanteResponse(){}

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }
}
