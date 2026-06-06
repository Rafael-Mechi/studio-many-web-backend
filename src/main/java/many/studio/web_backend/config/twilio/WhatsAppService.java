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
        Twilio.init(accountSid, authToken);
    }

    public void enviarMensagem(String telefoneDestino, String mensagem) {

        Message.creator(
                new PhoneNumber("whatsapp:" + telefoneDestino),
                new PhoneNumber(fromNumber),
                mensagem
        ).create();
    }
}