package many.studio.web_backend.dto.notificacao;

import java.util.Map;

/**
 * Contrato da mensagem publicada na fila de notificacao. Consumida pelo
 * studio-essencia-scheduling-actions-service, que exige "mensagem" e usa
 * "parametros" para resolver placeholders no formato {{chave}}.
 */
public record NotificacaoWhatsApp(String numero, String mensagem, Map<String, String> parametros) {
}
