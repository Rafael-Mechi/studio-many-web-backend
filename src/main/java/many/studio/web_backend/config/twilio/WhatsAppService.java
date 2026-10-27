package many.studio.web_backend.config.twilio;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class WhatsAppService {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.whatsapp-number}")
    private String fromNumber;

    @PostConstruct
    public void init() {
        if (accountSid == null || accountSid.isBlank()
                || authToken == null || authToken.isBlank()) {
            // Credenciais Twilio nao configuradas: servico fica inativo.
            return;
        }
        Twilio.init(accountSid, authToken);
    }

    public void enviarMensagem(String telefoneDestino, String mensagem) {
        if (accountSid == null || accountSid.isBlank()
                || authToken == null || authToken.isBlank()
                || fromNumber == null || fromNumber.isBlank()) {
            return;
        }

        Message.creator(
                new PhoneNumber("whatsapp:" + telefoneDestino),
                new PhoneNumber(fromNumber),
                mensagem
        ).create();
    }
}