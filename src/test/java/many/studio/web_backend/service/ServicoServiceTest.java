package many.studio.web_backend.service;

import many.studio.web_backend.dto.servico.ServicoCadastroDto;
import many.studio.web_backend.dto.servico.ServicoListarDto;
import many.studio.web_backend.entity.CategoriaServico;
import many.studio.web_backend.entity.Pacote;
import many.studio.web_backend.entity.Profissional;
import many.studio.web_backend.entity.Servico;
import many.studio.web_backend.entity.ServicoProfissional;
import many.studio.web_backend.exception.EntityConflictException;
import many.studio.web_backend.repository.CategoriaServicoRepository;
import many.studio.web_backend.repository.PacoteRepository;
import many.studio.web_backend.repository.ProfissionalRepository;
import many.studio.web_backend.repository.ServicoProfissionalRepository;
import many.studio.web_backend.repository.ServicoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServicoServiceTest {

    @Mock
    private PacoteRepository pacoteRepository;

    @Mock
    private ServicoRepository servicoRepository;

    @Mock
    private ServicoProfissionalRepository servicoProfissionalRepository;

    @Mock
    private ProfissionalRepository profissionalRepository;

    @Mock
    private CategoriaServicoRepository categoriaServicoRepository;

    @InjectMocks
    private ServicoService servicoService;

    private Servico criarServico() {
        Servico servico = new Servico();
        servico.setId(1L);
        servico.setNome("Limpeza de pele");
        servico.setDescricao("Procedimento para remover impurezas e cravos.");
        servico.setFotoUrl("https://exemplo.com/limpeza.jpg");
        servico.setDuracaoMinutos(40);
        servico.setPreco(120.00);
        servico.setAtivo(true);
        servico.setCriadoEm(LocalDateTime.now());
        return servico;
    }

    @Nested
    @DisplayName("Listar serviços")
    class Listar {

        @Test
        @DisplayName("Deve retornar os serviços convertidos em DTO")
        void deveRetornarUmaListaDeServicos() {
            Servico servico = criarServico();
            when(servicoRepository.findAll()).thenReturn(List.of(servico));
            when(servicoProfissionalRepository.findByServicoId(1L)).thenReturn(List.of());

            List<ServicoListarDto> resultado = servicoService.listar();

            assertEquals(1, resultado.size());
            assertEquals("Limpeza de pele", resultado.get(0).getNome());
            assertEquals(120.00, resultado.get(0).getPreco());
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não houver serviços")
        void deveRetornarListaVazia() {
            when(servicoRepository.findAll()).thenReturn(List.of());

            assertTrue(servicoService.listar().isEmpty());
        }
    }

    @Nested
    @DisplayName("Listar pacotes por serviço")
    class ListarPacotesPorServico {

        @Test
        @DisplayName("Deve retornar os pacotes ativos do serviço")
        void deveRetornarUmaListaDePacotesPorServico() {
            Servico servico = criarServico();

            Pacote pacote = new Pacote();
            pacote.setId(1L);
            pacote.setNome("Pacote inverno");
            pacote.setTotalSessoes(8);
            pacote.setPrecoTotal(490.00);
            pacote.setAtivo(true);
            pacote.setCriadoEm(LocalDateTime.now());
            pacote.setValidadeDias(90);
            pacote.setServico(servico);

            when(servicoRepository.existsById(1L)).thenReturn(true);
            when(pacoteRepository.findByServicoIdAndAtivoTrue(1L)).thenReturn(List.of(pacote));

            assertEquals(List.of(pacote), servicoService.listarPacotesPorServico(1L));
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando o serviço não tiver pacotes")
        void deveRetornarListaVazia() {
            when(servicoRepository.existsById(1L)).thenReturn(true);
            when(pacoteRepository.findByServicoIdAndAtivoTrue(1L)).thenReturn(List.of());

            assertTrue(servicoService.listarPacotesPorServico(1L).isEmpty());
        }
    }

    @Nested
    @DisplayName("Listar serviços por profissional")
    class ListarServicosPorProfissional {

        @Test
        @DisplayName("Deve retornar os serviços vinculados ao profissional")
        void deveRetornarUmaListaDeServicosPorProfissional() {
            Servico servico = criarServico();
            Profissional profissional = new Profissional();
            profissional.setId(1L);
            profissional.setNome("Isabelly");

            ServicoProfissional vinculo = new ServicoProfissional(servico, profissional);

            when(profissionalRepository.existsById(1L)).thenReturn(true);
            when(servicoProfissionalRepository.findAllByProfissionalId(1L))
                    .thenReturn(List.of(vinculo));

            assertEquals(List.of(servico), servicoService.listarServicosPorProfissional(1L));
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando o profissional não tiver serviços")
        void deveRetornarListaVazia() {
            when(profissionalRepository.existsById(1L)).thenReturn(true);
            when(servicoProfissionalRepository.findAllByProfissionalId(1L))
                    .thenReturn(List.of());

            assertTrue(servicoService.listarServicosPorProfissional(1L).isEmpty());
        }
    }

    @Nested
    @DisplayName("Criar serviço")
    class Criar {

        @Test
        @DisplayName("Deve criar um serviço com sucesso")
        void deveCriarUmServicoComSucesso() {
            Servico servico = criarServico();
            CategoriaServico categoria = new CategoriaServico();
            categoria.setId(1L);
            categoria.setCategoria("Facial");

            ServicoCadastroDto dto = new ServicoCadastroDto();
            dto.setNome(servico.getNome());
            dto.setDescricao(servico.getDescricao());
            dto.setFotoUrl(servico.getFotoUrl());
            dto.setDuracaoMinutos(servico.getDuracaoMinutos());
            dto.setPreco(servico.getPreco());
            dto.setAtivo(true);
            dto.setCategoriaId(1L);

            when(servicoRepository.existsByNome(servico.getNome())).thenReturn(false);
            when(categoriaServicoRepository.findById(1L)).thenReturn(Optional.of(categoria));
            when(servicoRepository.save(any(Servico.class))).thenReturn(servico);
            when(servicoProfissionalRepository.findByServicoId(1L)).thenReturn(List.of());
            when(pacoteRepository.findByServicoId(1L)).thenReturn(List.of());
            when(pacoteRepository.save(any(Pacote.class))).thenAnswer(invocation -> invocation.getArgument(0));

            ServicoListarDto resultado = servicoService.criar(dto);

            assertEquals(servico.getId(), resultado.getId());
            assertEquals(servico.getNome(), resultado.getNome());
            assertEquals(servico.getPreco(), resultado.getPreco());
        }

        @Test
        @DisplayName("Deve recusar serviço com nome já cadastrado")
        void deveRetornarUmaExceptionSeServicoExistePorNome() {
            ServicoCadastroDto dto = new ServicoCadastroDto();
            dto.setNome("Limpeza de pele");

            when(servicoRepository.existsByNome("Limpeza de pele")).thenReturn(true);

            assertThrows(
                    EntityConflictException.class,
                    () -> servicoService.criar(dto)
            );
        }
    }
}
