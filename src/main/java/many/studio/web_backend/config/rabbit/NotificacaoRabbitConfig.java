package many.studio.web_backend.config.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Declara a topologia de notificacoes consumida pelo microservico de acoes.
 * Contrato: payload JSON {@code {"numero": "...", "mensagem": "...",
 * "parametros": {...}}}.
 *
 * A fila possui DLQ IDENTICA a declarada pelo microservico de acoes
 * (x-dead-letter-exchange/routing-key): declaracoes inequivalentes rejeitam o
 * canal (406 PRECONDITION_FAILED) e quebram a publicacao/consumo.
 */
@Configuration
public class NotificacaoRabbitConfig {

    @Value("${notificacao.exchange}")
    private String exchange;

    @Value("${notificacao.queue}")
    private String queue;

    @Value("${notificacao.routing-key}")
    private String routingKey;

    @Value("${notificacao.dlx-exchange}")
    private String dlxExchange;

    @Value("${notificacao.dlq}")
    private String dlq;

    @Bean
    public DirectExchange exchangeNotificacao() {
        return new DirectExchange(exchange, true, false);
    }

    @Bean
    public DirectExchange exchangeNotificacaoDlq() {
        return new DirectExchange(dlxExchange, true, false);
    }

    @Bean
    public Queue filaNotificacaoWhatsapp() {
        return QueueBuilder.durable(queue)
                .deadLetterExchange(dlxExchange)
                .deadLetterRoutingKey(routingKey)
                .build();
    }

    @Bean
    public Queue filaNotificacaoWhatsappDlq() {
        return QueueBuilder.durable(dlq).build();
    }

    @Bean
    public Binding bindingNotificacaoWhatsapp(Queue filaNotificacaoWhatsapp, DirectExchange exchangeNotificacao) {
        return BindingBuilder.bind(filaNotificacaoWhatsapp)
                .to(exchangeNotificacao)
                .with(routingKey);
    }

    @Bean
    public Binding bindingNotificacaoWhatsappDlq(Queue filaNotificacaoWhatsappDlq, DirectExchange exchangeNotificacaoDlq) {
        return BindingBuilder.bind(filaNotificacaoWhatsappDlq)
                .to(exchangeNotificacaoDlq)
                .with(routingKey);
    }

    @Bean
    public MessageConverter jsonConverter() {
        return new JacksonJsonMessageConverter();
    }
}
