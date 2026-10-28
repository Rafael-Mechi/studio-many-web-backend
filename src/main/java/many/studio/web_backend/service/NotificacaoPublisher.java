package many.studio.web_backend.service;

import many.studio.web_backend.dto.notificacao.NotificacaoWhatsApp;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Publica notificações de WhatsApp no RabbitMQ. O envio efetivo fica a cargo do
 * microserviço de ações.
 */
@Service
public class NotificacaoPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${notificacao.exchange}")
    private String exchange;

    @Value("${notificacao.routing-key}")
    private String routingKey;

    public NotificacaoPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarWhatsApp(String numero, String texto) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, new NotificacaoWhatsApp(numero, texto, null));
        } catch (AmqpException ex) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Nao foi possivel enviar a notificacao no momento. Tente novamente.");
        }
    }
}
