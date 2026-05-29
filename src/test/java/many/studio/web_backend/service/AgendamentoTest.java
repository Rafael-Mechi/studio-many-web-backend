package many.studio.web_backend.service;

import many.studio.web_backend.dto.agendamento.CancelarAgendamentoRequest;
import many.studio.web_backend.entity.*;
import many.studio.web_backend.exception.NonAuthorizedException;
import many.studio.web_backend.repository.*;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.service.helper.AgendamentoHelper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AgendamentoTest {
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

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AgendamentoService service;

    @Nested
    class CancelarAgendamento{
        @Test
        void deveCancelarQuandoUsuarioForDonoDoAgendamentoEFaltarMaisDe24Horas() {

            Long idAgendamento = 1L;
            Long idUsuario = 2L;

            CancelarAgendamentoRequest request = new CancelarAgendamentoRequest();
            request.setMotivo("Cliente cancelou");

            Perfil perfilCliente = new Perfil();
            perfilCliente.setId(3L);

            Usuario usuario = new Usuario();
            usuario.setId(idUsuario);
            usuario.setPerfil(perfilCliente);

            Agendamento agendamento = new Agendamento();
            agendamento.setId(idAgendamento);

            AgendamentoItem item = new AgendamentoItem();
            item.setInicioAtendimento(LocalDateTime.now().plusHours(30));

            when(agendamentoItemRepository.findByAgendamentoId(idAgendamento))
                    .thenReturn(List.of(item));
            StatusAgendamento statusCancelado = new StatusAgendamento();
            statusCancelado.setId(5L);
            statusCancelado.setEstado("cancelado");

            when(agendamentoHelper
                    .isUsuarioValidoParaCancelamentoDeAgendamento(idUsuario, idAgendamento))
                    .thenReturn(true);

            when(agendamentoRepository.existsById(idAgendamento))
                    .thenReturn(true);

            when(agendamentoRepository.findById(idAgendamento))
                    .thenReturn(Optional.of(agendamento));

            when(usuarioRepository.findById(idUsuario))
                    .thenReturn(Optional.of(usuario));

            when(statusAgendamentoRepository.findByEstado("cancelado"))
                    .thenReturn(Optional.of(statusCancelado));

            doNothing().when(agendamentoItemRepository)
                    .deleteByAgendamentoId(idAgendamento);

            service.cancelarAgendamento(idAgendamento, request, idUsuario);

            verify(agendamentoItemRepository)
                    .deleteByAgendamentoId(idAgendamento);

            verify(statusAgendamentoRepository)
                    .findByEstado("cancelado");

            verify(agendamentoRepository)
                    .save(agendamento);

            assertEquals("cancelado",
                    agendamento.getStatusAgendamento().getEstado());

            assertEquals("Cliente cancelou",
                    agendamento.getCancelamentoMotivo());

            assertNotNull(agendamento.getCanceladoEm());
        }

        @Test
        void deveCancelarQuandoUsuarioForAdminMesmoFaltandoMenosDe24Horas() {

            Long idAgendamento = 1L;
            Long idUsuario = 1L;

            CancelarAgendamentoRequest request = new CancelarAgendamentoRequest();
            request.setMotivo("Admin cancelou");

            Perfil perfilAdmin = new Perfil();
            perfilAdmin.setId(1L);

            Usuario admin = new Usuario();
            admin.setId(idUsuario);
            admin.setPerfil(perfilAdmin);

            Agendamento agendamento = new Agendamento();
            agendamento.setId(idAgendamento);

            AgendamentoItem item = new AgendamentoItem();
            item.setInicioAtendimento(LocalDateTime.now().plusHours(2));

            when(agendamentoItemRepository.findByAgendamentoId(idAgendamento))
                    .thenReturn(List.of(item));

            StatusAgendamento statusCancelado = new StatusAgendamento();
            statusCancelado.setEstado("cancelado");

            when(agendamentoHelper
                    .isUsuarioValidoParaCancelamentoDeAgendamento(idUsuario, idAgendamento))
                    .thenReturn(true);

            when(agendamentoRepository.existsById(idAgendamento))
                    .thenReturn(true);

            when(agendamentoRepository.findById(idAgendamento))
                    .thenReturn(Optional.of(agendamento));

            when(usuarioRepository.findById(idUsuario))
                    .thenReturn(Optional.of(admin));

            when(statusAgendamentoRepository.findByEstado("cancelado"))
                    .thenReturn(Optional.of(statusCancelado));

            service.cancelarAgendamento(idAgendamento, request, idUsuario);

            verify(agendamentoItemRepository)
                    .deleteByAgendamentoId(idAgendamento);

            verify(agendamentoRepository)
                    .save(agendamento);

            assertEquals("cancelado",
                    agendamento.getStatusAgendamento().getEstado());

            assertEquals("Admin cancelou",
                    agendamento.getCancelamentoMotivo());
        }

        @Test
        void deveCancelarQuandoUsuarioForAdminMesmoDepoisDas24Horas() {

            Long idAgendamento = 1L;
            Long idUsuario = 1L;

            CancelarAgendamentoRequest request = new CancelarAgendamentoRequest();
            request.setMotivo("Admin cancelou atrasado");

            Perfil perfilAdmin = new Perfil();
            perfilAdmin.setId(1L);

            Usuario admin = new Usuario();
            admin.setId(idUsuario);
            admin.setPerfil(perfilAdmin);

            Agendamento agendamento = new Agendamento();
            agendamento.setId(idAgendamento);

            AgendamentoItem item = new AgendamentoItem();
            item.setInicioAtendimento(LocalDateTime.now().plusHours(2));

            when(agendamentoItemRepository.findByAgendamentoId(idAgendamento))
                    .thenReturn(List.of(item));

            StatusAgendamento statusCancelado = new StatusAgendamento();
            statusCancelado.setEstado("cancelado");

            when(agendamentoHelper
                    .isUsuarioValidoParaCancelamentoDeAgendamento(idUsuario, idAgendamento))
                    .thenReturn(true);

            when(agendamentoRepository.existsById(idAgendamento))
                    .thenReturn(true);

            when(agendamentoRepository.findById(idAgendamento))
                    .thenReturn(Optional.of(agendamento));

            when(usuarioRepository.findById(idUsuario))
                    .thenReturn(Optional.of(admin));

            when(statusAgendamentoRepository.findByEstado("cancelado"))
                    .thenReturn(Optional.of(statusCancelado));

            service.cancelarAgendamento(idAgendamento, request, idUsuario);

            verify(agendamentoItemRepository)
                    .deleteByAgendamentoId(idAgendamento);

            verify(agendamentoRepository)
                    .save(agendamento);

            assertEquals("cancelado",
                    agendamento.getStatusAgendamento().getEstado());
        }

        @Test
        void naoDeveCancelarQuandoFaltarMenosDe24HorasEUsuarioNaoForAdmin() {

            Long idAgendamento = 1L;
            Long idUsuario = 2L;

            CancelarAgendamentoRequest request = new CancelarAgendamentoRequest();

            Perfil perfilCliente = new Perfil();
            perfilCliente.setId(3L);

            Usuario usuario = new Usuario();
            usuario.setId(idUsuario);
            usuario.setPerfil(perfilCliente);

            Agendamento agendamento = new Agendamento();
            agendamento.setId(idAgendamento);

            AgendamentoItem item = new AgendamentoItem();
            item.setInicioAtendimento(LocalDateTime.now().plusHours(5));

            when(agendamentoItemRepository.findByAgendamentoId(idAgendamento))
                    .thenReturn(List.of(item));

            when(agendamentoHelper
                    .isUsuarioValidoParaCancelamentoDeAgendamento(idUsuario, idAgendamento))
                    .thenReturn(true);

            when(agendamentoRepository.existsById(idAgendamento))
                    .thenReturn(true);

            when(usuarioRepository.findById(idUsuario))
                    .thenReturn(Optional.of(usuario));

            assertThrows(
                    NonAuthorizedException.class,
                    () -> service.cancelarAgendamento(idAgendamento, request, idUsuario)
            );

            verify(agendamentoItemRepository, never())
                    .deleteByAgendamentoId(any());

            verify(statusAgendamentoRepository, never())
                    .findByEstado(any());

            verify(agendamentoRepository, never())
                    .save(any());
        }

        @Test
        void naoDeveCancelarQuandoAgendamentoForDeOutroUsuario() {

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

            verify(agendamentoRepository, never())
                    .existsById(any());

            verify(agendamentoRepository, never())
                    .findById(any());

            verify(agendamentoItemRepository, never())
                    .deleteByAgendamentoId(any());

            verify(statusAgendamentoRepository, never())
                    .findByEstado(any());

            verify(agendamentoRepository, never())
                    .save(any());

            verify(agendamentoItemRepository, never())
                    .findByAgendamentoId(any());
        }
    }
}
