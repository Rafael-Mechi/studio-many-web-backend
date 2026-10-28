package many.studio.web_backend.service;

import many.studio.web_backend.entity.Agendamento;
import many.studio.web_backend.entity.AgendamentoItem;
import many.studio.web_backend.entity.Cliente;
import many.studio.web_backend.entity.Pacote;
import many.studio.web_backend.entity.Profissional;
import many.studio.web_backend.entity.Servico;
import many.studio.web_backend.entity.StatusAgendamento;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class NotificacaoAgendamentoServiceTest {

    @Mock
    private NotificacaoPublisher notificacaoPublisher;

    @InjectMocks
    private NotificacaoAgendamentoService service;

    @Test
    @DisplayName("Deve enviar o resumo completo quando o agendamento for concluído")
    void deveEnviarResumoCompletoNaConclusao() {
        Agendamento agendamento = criarAgendamento(StatusAgendamento.CONCLUIDO);
        ArgumentCaptor<String> mensagem = ArgumentCaptor.forClass(String.class);

        service.notificarStatus(agendamento);

        verify(notificacaoPublisher).enviarWhatsApp(eq("11999999999"), mensagem.capture());
        assertTrue(mensagem.getValue().contains("RESUMO DO AGENDAMENTO"));
        assertTrue(mensagem.getValue().contains("Sessões (2)"));
        assertTrue(mensagem.getValue().contains("10/11/2026 às 09:00"));
        assertTrue(mensagem.getValue().contains("17/11/2026 às 09:00"));
        assertTrue(mensagem.getValue().contains("600,00"));
    }

    @Test
    @DisplayName("Deve notificar o cliente quando o status for no-show")
    void deveNotificarNoShow() {
        Agendamento agendamento = criarAgendamento(StatusAgendamento.NO_SHOW);
        ArgumentCaptor<String> mensagem = ArgumentCaptor.forClass(String.class);

        service.notificarStatus(agendamento);

        verify(notificacaoPublisher).enviarWhatsApp(eq("11999999999"), mensagem.capture());
        assertTrue(mensagem.getValue().contains("Sentimos sua falta"));
        assertTrue(mensagem.getValue().contains("Limpeza de pele"));
    }

    @Test
    @DisplayName("Não deve notificar o cliente em check-in ou em atendimento")
    void naoDeveNotificarEtapasOperacionais() {
        service.notificarStatus(criarAgendamento(StatusAgendamento.CHECK_IN));
        service.notificarStatus(criarAgendamento(StatusAgendamento.EM_ATENDIMENTO));

        verifyNoInteractions(notificacaoPublisher);
    }

    private Agendamento criarAgendamento(String estado) {
        Servico servico = new Servico();
        servico.setNome("Limpeza de pele");
        servico.setDuracaoMinutos(60);

        Pacote pacote = new Pacote();
        pacote.setNome("Combo Verão 2x");
        pacote.setTotalSessoes(2);
        pacote.setPrecoTotal(600.00);
        pacote.setServico(servico);

        Profissional profissional = new Profissional();
        profissional.setNome("Isabelly");
        profissional.setTelefone("11977776666");

        Cliente cliente = new Cliente();
        cliente.setNome("Ana");
        cliente.setTelefone("11999999999");

        StatusAgendamento status = new StatusAgendamento();
        status.setEstado(estado);

        Agendamento agendamento = new Agendamento();
        agendamento.setId(1L);
        agendamento.setCliente(cliente);
        agendamento.setPacote(pacote);
        agendamento.setProfissional(profissional);
        agendamento.setStatusAgendamento(status);
        agendamento.setPrecoFinal(600.00);

        AgendamentoItem primeira = new AgendamentoItem();
        primeira.setInicioAtendimento(LocalDateTime.of(2026, 11, 10, 9, 0));
        primeira.setFimAtendimento(LocalDateTime.of(2026, 11, 10, 10, 0));
        primeira.setServico(servico);
        primeira.setProfissional(profissional);

        AgendamentoItem segunda = new AgendamentoItem();
        segunda.setInicioAtendimento(LocalDateTime.of(2026, 11, 17, 9, 0));
        segunda.setFimAtendimento(LocalDateTime.of(2026, 11, 17, 10, 0));
        segunda.setServico(servico);
        segunda.setProfissional(profissional);

        agendamento.setItens(List.of(primeira, segunda));
        return agendamento;
    }
}
