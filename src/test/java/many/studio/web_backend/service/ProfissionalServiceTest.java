package many.studio.web_backend.service;

import many.studio.web_backend.dto.profissional.*;
import many.studio.web_backend.entity.Cliente;
import many.studio.web_backend.entity.Profissional;
import many.studio.web_backend.entity.Usuario;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.repository.AgendamentoRepository;
import many.studio.web_backend.repository.ClienteAgregado;
import many.studio.web_backend.repository.ClienteRepository;
import many.studio.web_backend.repository.ProfissionalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfissionalServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ProfissionalRepository profissionalRepository;

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @InjectMocks
    private ProfissionalService profissionalService;

    private Profissional profissional;
    private Usuario usuario;
    private Cliente cliente;
    private ClienteAgregado clienteAgregadoMock;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("carlos@email.com");
        usuario.setSenha("senha123");
        usuario.setAtivo(true);

        profissional = new Profissional();
        profissional.setId(1L);
        profissional.setNome("Carlos");
        profissional.setUsuario(usuario);

        cliente = new Cliente();
        cliente.setId(10L);
        cliente.setNome("Giovana Lopes");
        cliente.setTelefone("(11) 98765-4321");
        cliente.setDocumento("123.456.789-01");
        cliente.setTotalNoShows(0);
        cliente.setUsuario(usuario);

        // Mock da projeção de interface do banco de dados
        clienteAgregadoMock = mock(ClienteAgregado.class);
    }

    @Nested
    @DisplayName("Testes do método listarClientesPorProfissionalId")
    class ListarClientesTests {

        @Test
        @DisplayName("Deve retornar a lista de clientes com sucesso")
        void deveRetornarListaDeClientesComSucesso() {
            when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
            when(clienteRepository.findClientesByProfissionalId(1L)).thenReturn(List.of(clienteAgregadoMock));
            when(clienteAgregadoMock.getCliente()).thenReturn(cliente);
            when(clienteAgregadoMock.getUltimaVisita()).thenReturn(LocalDate.now());
            when(clienteAgregadoMock.getTotalGasto()).thenReturn(250.0);
            when(agendamentoRepository.findServicoPreferidoByClienteId(10L)).thenReturn(List.of("Depilação a laser"));

            List<ClientePorProfissionalDto> resultado = profissionalService.listarClientesPorProfissionalId(1L);

            assertNotNull(resultado);
            assertEquals(1, resultado.size());
            assertEquals("Giovana Lopes", resultado.get(0).getNomeCliente());
            assertEquals("Depilação a laser", resultado.get(0).getServicoPreferido());
        }

        @Test
        @DisplayName("Deve lançar exceção quando o profissional não existir")
        void deveLancarExcecaoQuandoProfissionalNaoExistir() {
            when(profissionalRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class, () ->
                    profissionalService.listarClientesPorProfissionalId(1L)
            );
        }

        @Test
        @DisplayName("Deve lançar exceção quando a lista de agregados for vazia")
        void deveLancarExcecaoQuandoListaAgregadosForVazia() {
            when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
            when(clienteRepository.findClientesByProfissionalId(1L)).thenReturn(Collections.emptyList());

            assertThrows(EntityNotFoundException.class, () ->
                    profissionalService.listarClientesPorProfissionalId(1L)
            );
        }
    }

    @Nested
    @DisplayName("Testes do método detalharClientePorProfissional")
    class DetalharClienteTests {

        @Test
        @DisplayName("Deve retornar os detalhes do cliente sem a última visita e com histórico recente")
        void deveRetornarDetalhesDoClienteComSucesso() {
            AgendamentoHistoricoDto historicoItem = new AgendamentoHistoricoDto(
                    "Depilação a laser", LocalDateTime.now(), "Joana", "CONCLUÍDO"
            );

            when(profissionalRepository.existsById(1L)).thenReturn(true);
            when(clienteRepository.findClienteByProfissionalIdEClienteId(1L, 10L)).thenReturn(Optional.of(clienteAgregadoMock));
            when(clienteAgregadoMock.getCliente()).thenReturn(cliente);
            when(clienteAgregadoMock.getTotalGasto()).thenReturn(250.0);
            when(agendamentoRepository.findHistoricoRecenteByClienteId(10L)).thenReturn(List.of(historicoItem));

            ClienteDetalheDto resultado = profissionalService.detalharClientePorProfissional(1L, 10L);

            assertNotNull(resultado);
            assertEquals("Giovana Lopes", resultado.nome());
            assertEquals(1, resultado.historicoRecente().size());
            assertEquals("Depilação a laser", resultado.historicoRecente().get(0).servico());
        }

        @Test
        @DisplayName("Deve lançar exceção se o cliente não estiver associado ao profissional")
        void deveLancarExcecaoSeClienteNaoAssociadoAoProfissional() {
            when(profissionalRepository.existsById(1L)).thenReturn(true);
            when(clienteRepository.findClienteByProfissionalIdEClienteId(1L, 10L)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class, () ->
                    profissionalService.detalharClientePorProfissional(1L, 10L)
            );
        }
    }

    @Nested
    @DisplayName("Testes do método atualizarProfissional")
    class AtualizarProfissionalTests {

        @Test
        @DisplayName("Deve atualizar os dados do profissional e do usuário de forma casada")
        void deveAtualizarProfissionalEUsuarioComSucesso() {
            ProfissionalUpdateDto updateDto = new ProfissionalUpdateDto("Carlos Souza", "carlos.souza@email.com", "novaSenha123");
            when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));

            ProfissionalResponseDto resultado = profissionalService.atualizarProfissional(1L, updateDto);

            assertNotNull(resultado);
            assertEquals("Carlos Souza", profissional.getNome());
            assertEquals("carlos.souza@email.com", profissional.getUsuario().getEmail());
            assertEquals("novaSenha123", profissional.getUsuario().getSenha());
            verify(profissionalRepository, times(1)).save(profissional);
        }
    }

    @Nested
    @DisplayName("Testes do método deletarProfissional (Soft Delete)")
    class DeletarProfissionalTests {

        @Test
        @DisplayName("Deve realizar o soft delete inativando o campo ativo do usuário")
        void deveRealizarSoftDeleteInativandoUsuario() {
            when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));

            profissionalService.deletarProfissional(1L);

            assertFalse(profissional.getUsuario().getAtivo());
            verify(profissionalRepository, times(1)).save(profissional);
        }
    }
}