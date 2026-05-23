package many.studio.web_backend.service;

import many.studio.web_backend.dto.agendamento.CancelarAgendamentoRequest;
import many.studio.web_backend.entity.Agendamento;
import many.studio.web_backend.entity.Perfil;
import many.studio.web_backend.exception.NonAuthorizedException;
import many.studio.web_backend.repository.AgendamentoItemRepository;
import many.studio.web_backend.repository.AgendamentoRepository;
import many.studio.web_backend.repository.PerfilRepository;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.repository.StatusAgendamentoRepository;
import many.studio.web_backend.service.helper.AgendamentoHelper;
import many.studio.web_backend.entity.StatusAgendamento;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AgendamentoTest {
    @InjectMocks
    private AgendamentoService service;

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @Mock
    private AgendamentoItemRepository agendamentoItemRepository;

    @Mock
    private StatusAgendamentoRepository statusAgendamentoRepository;

    @Mock
    private PerfilRepository perfilRepository;

    @Mock
    private AgendamentoHelper agendamentoHelper;

    @Nested
    class CancelarAgendamento{
        @Test
        void deveLancarExcecaoQuandoUsuarioNaoAutorizado() {

            Long idAgendamento = 1L;
            Long idUsuario = 2L;

            CancelarAgendamentoRequest request = new CancelarAgendamentoRequest();

            when(agendamentoHelper
                    .isUsuarioValidoParaCancelamentoDeAgendamento(idUsuario, idAgendamento))
                    .thenReturn(false);

            assertThrows(
                    NonAuthorizedException.class,
                    () -> service.cancelarAgendamento(idAgendamento, request, idUsuario)
            );

            verify(agendamentoRepository, never()).findById(any());
            verify(agendamentoItemRepository, never()).deleteByAgendamentoId(any());
            verify(statusAgendamentoRepository, never()).findByEstado(any());
            verify(agendamentoRepository, never()).save(any());
        }

        @Test
        void deveLancarExcecaoQuandoAgendamentoNaoExiste() {

            Long idAgendamento = 1L;
            Long idUsuario = 2L;

            CancelarAgendamentoRequest request = new CancelarAgendamentoRequest();

            when(agendamentoHelper
                    .isUsuarioValidoParaCancelamentoDeAgendamento(idUsuario, idAgendamento))
                    .thenReturn(true);

            when(agendamentoRepository.existsById(idAgendamento))
                    .thenReturn(false);

            assertThrows(
                    EntityNotFoundException.class,
                    () -> service.cancelarAgendamento(idAgendamento, request, idUsuario)
            );

            verify(agendamentoRepository).existsById(idAgendamento);
            verify(agendamentoRepository, never()).findById(any());
            verify(agendamentoItemRepository, never()).deleteByAgendamentoId(any());
            verify(statusAgendamentoRepository, never()).findByEstado(any());
            verify(agendamentoRepository, never()).save(any());
        }

        @Test
        void deveLancarExcecaoQuandoFaltarMenosDe24HorasEUsuarioNaoForAdmin() {

            Long idAgendamento = 1L;
            Long idUsuario = 2L;

            CancelarAgendamentoRequest request = new CancelarAgendamentoRequest();

            Perfil perfil = new Perfil();
            perfil.setId(2L);

            Agendamento agendamento = new Agendamento();
            agendamento.setId(idAgendamento);
            agendamento.setInicio(LocalDateTime.now().plusHours(10));

            when(agendamentoHelper
                    .isUsuarioValidoParaCancelamentoDeAgendamento(idUsuario, idAgendamento))
                    .thenReturn(true);

            when(agendamentoRepository.existsById(idAgendamento))
                    .thenReturn(true);

            when(agendamentoRepository.findById(idAgendamento))
                    .thenReturn(Optional.of(agendamento));

            when(perfilRepository.findByUsuarioId(idUsuario))
                    .thenReturn(perfil);

            assertThrows(
                    NonAuthorizedException.class,
                    () -> service.cancelarAgendamento(idAgendamento, request, idUsuario)
            );

            verify(agendamentoRepository).existsById(idAgendamento);
            verify(agendamentoRepository).findById(idAgendamento);
            verify(perfilRepository).findByUsuarioId(idUsuario);
            verify(agendamentoItemRepository, never()).deleteByAgendamentoId(any());
            verify(statusAgendamentoRepository, never()).findByEstado(any());
            verify(agendamentoRepository, never()).save(any());
        }

        @Test
        void devePermitirCancelamentoQuandoFaltarMenosDe24HorasMasUsuarioForAdmin() {

            Long idAgendamento = 1L;
            Long idUsuario = 2L;

            CancelarAgendamentoRequest request = new CancelarAgendamentoRequest();
            request.setMotivo("Cancelamento solicitado pelo admin");

            Perfil perfil = new Perfil();
            perfil.setId(1L);

            Agendamento agendamento = new Agendamento();
            agendamento.setId(idAgendamento);
            agendamento.setInicio(LocalDateTime.now().plusHours(10));

            StatusAgendamento statusCancelado = new StatusAgendamento();
            statusCancelado.setId(1L);
            statusCancelado.setEstado("cancelado");

            when(agendamentoHelper
                    .isUsuarioValidoParaCancelamentoDeAgendamento(idUsuario, idAgendamento))
                    .thenReturn(true);

            when(agendamentoRepository.existsById(idAgendamento))
                    .thenReturn(true);

            when(agendamentoRepository.findById(idAgendamento))
                    .thenReturn(Optional.of(agendamento));

            when(perfilRepository.findByUsuarioId(idUsuario))
                    .thenReturn(perfil);

            when(statusAgendamentoRepository.findByEstado("cancelado"))
                    .thenReturn(Optional.of(statusCancelado));

            doNothing().when(agendamentoItemRepository).deleteByAgendamentoId(idAgendamento);

            service.cancelarAgendamento(idAgendamento, request, idUsuario);

            verify(agendamentoItemRepository).deleteByAgendamentoId(idAgendamento);
            verify(statusAgendamentoRepository).findByEstado("cancelado");
            verify(agendamentoRepository).save(agendamento);

            assertNotNull(agendamento.getCanceladoEm());
            assertEquals("cancelado", agendamento.getStatusAgendamento().getEstado());
            assertEquals("Cancelamento solicitado pelo admin", agendamento.getCancelamentoMotivo());
        }

        @Test
        void devePermitirCancelamentoQuandoFaltarMaisDe24Horas() {

            Long idAgendamento = 1L;
            Long idUsuario = 2L;

            CancelarAgendamentoRequest request = new CancelarAgendamentoRequest();
            request.setMotivo("Cliente mudou de ideia");

            Perfil perfil = new Perfil();
            perfil.setId(2L);

            Agendamento agendamento = new Agendamento();
            agendamento.setId(idAgendamento);
            agendamento.setInicio(LocalDateTime.now().plusHours(48));

            StatusAgendamento statusCancelado = new StatusAgendamento();
            statusCancelado.setId(1L);
            statusCancelado.setEstado("cancelado");

            when(agendamentoHelper
                    .isUsuarioValidoParaCancelamentoDeAgendamento(idUsuario, idAgendamento))
                    .thenReturn(true);

            when(agendamentoRepository.existsById(idAgendamento))
                    .thenReturn(true);

            when(agendamentoRepository.findById(idAgendamento))
                    .thenReturn(Optional.of(agendamento));

            when(perfilRepository.findByUsuarioId(idUsuario))
                    .thenReturn(perfil);

            when(statusAgendamentoRepository.findByEstado("cancelado"))
                    .thenReturn(Optional.of(statusCancelado));

            doNothing().when(agendamentoItemRepository).deleteByAgendamentoId(idAgendamento);

            service.cancelarAgendamento(idAgendamento, request, idUsuario);

            verify(agendamentoItemRepository).deleteByAgendamentoId(idAgendamento);
            verify(statusAgendamentoRepository).findByEstado("cancelado");
            verify(agendamentoRepository).save(agendamento);

            assertNotNull(agendamento.getCanceladoEm());
            assertEquals("cancelado", agendamento.getStatusAgendamento().getEstado());
            assertEquals("Cliente mudou de ideia", agendamento.getCancelamentoMotivo());
        }

        @Test
        void deveLancarRuntimeExceptionQuandoStatusCanceladoNaoEncontrado() {

            Long idAgendamento = 1L;
            Long idUsuario = 2L;

            CancelarAgendamentoRequest request = new CancelarAgendamentoRequest();
            request.setMotivo("Cancelamento");

            Perfil perfil = new Perfil();
            perfil.setId(1L);

            Agendamento agendamento = new Agendamento();
            agendamento.setId(idAgendamento);
            agendamento.setInicio(LocalDateTime.now().plusHours(10));

            when(agendamentoHelper
                    .isUsuarioValidoParaCancelamentoDeAgendamento(idUsuario, idAgendamento))
                    .thenReturn(true);

            when(agendamentoRepository.existsById(idAgendamento))
                    .thenReturn(true);

            when(agendamentoRepository.findById(idAgendamento))
                    .thenReturn(Optional.of(agendamento));

            when(perfilRepository.findByUsuarioId(idUsuario))
                    .thenReturn(perfil);

            when(statusAgendamentoRepository.findByEstado("cancelado"))
                    .thenReturn(Optional.empty());

            assertThrows(
                    RuntimeException.class,
                    () -> service.cancelarAgendamento(idAgendamento, request, idUsuario)
            );

            verify(agendamentoItemRepository).deleteByAgendamentoId(idAgendamento);
            verify(statusAgendamentoRepository).findByEstado("cancelado");
            verify(agendamentoRepository, never()).save(any());
        }
    }
}
