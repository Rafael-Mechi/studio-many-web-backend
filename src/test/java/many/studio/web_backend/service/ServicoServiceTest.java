package many.studio.web_backend.service;

import many.studio.web_backend.dto.servico.ServicoCadastroDto;
import many.studio.web_backend.entity.Pacote;
import many.studio.web_backend.entity.Servico;
import many.studio.web_backend.exception.EntityConflictException;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.repository.PacoteRepository;
import many.studio.web_backend.repository.ServicoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ServicoServiceTest {

   @Mock
   private PacoteRepository pacoteRepository;

   @Mock
   private ServicoRepository servicoRepository;

   @InjectMocks
    private ServicoService servicoService;

   @Nested
   @DisplayName("Listar")
    class Listar{

       @Test
       @DisplayName("Deve retornar uma lista de servicos")
       void DeveRetornarUmaListaDeServico(){
          Servico servico = new Servico();
          servico.setId(1L);
          servico.setNome("Limpeza de pele");
          servico.setDescricao("Procedimento de remover impurezas, células mortas, cravos e miliuns da superfície do rosto.");
          servico.setFotoUrl("https://[NOME_DO_BUCKET].s3.[REGIÃO]://[CAMINHO/DA/IMAGEM.jpg]");
          servico.setDuracaoMinutos(40);
          servico.setPreco(120.00);
          servico.setAtivo(true);
          servico.setCriadoEm(LocalDateTime.now());

          List<Servico> list = new ArrayList<>();
          list.add(servico);

          Mockito.when(servicoRepository.findAll()).thenReturn(list);

          assertIterableEquals(list, servicoService.listar());
       }

      @Test
      @DisplayName("Deve retornar uma exception se lista vazia")
      void DeveRetornarUmaExceptionSeListaVazia(){
         List<Servico> list = new ArrayList<>();

         Mockito.when(servicoRepository.findAll()).thenReturn(list);

         Assertions.assertThrows(
                 EntityNotFoundException.class,
                 () -> servicoService.listar());

      }


   }

   @Nested
   @DisplayName("Pacotes")
   class ListarPacotesPorServico{

      @Test
      @DisplayName("Deve retornar uma lista de pacotes por serviço")
      void deveRetornarUmaListaDePacotesPorServico() {
         Servico servico = new Servico();
         servico.setId(1L);
         servico.setNome("Limpeza de pele");
         servico.setDescricao("Procedimento de remover impurezas, células mortas, cravos e miliuns da superfície do rosto.");
         servico.setFotoUrl("https://[NOME_DO_BUCKET].s3.[REGIÃO]://[CAMINHO/DA/IMAGEM.jpg]");
         servico.setDuracaoMinutos(40);
         servico.setPreco(120.00);
         servico.setAtivo(true);
         servico.setCriadoEm(LocalDateTime.now());

         Pacote pacote = new Pacote();
         pacote.setId(1L);
         pacote.setNome("pacote inverno limpeza de pele");
         pacote.setTotalSessoes(8);
         pacote.setPrecoTotal(490.00);
         pacote.setAtivo(true);
         pacote.setCriadoEm(LocalDateTime.now());
         pacote.setValidadeDias(90);
         pacote.setServico(servico);

         List<Pacote> list = new ArrayList <>();
         list.add(pacote);

         Mockito.when(pacoteRepository.findByServicoId(1L)).thenReturn(list);

         assertIterableEquals(list, servicoService.listarPacotesPorServico(1L));
      }

      @Test
      @DisplayName("Deve retornar exception se nenhum pacote encontrado para o serviço")
      void deveRetornarExceptionSeListaVazia() {
         List<Pacote> list = new ArrayList<>();

         Mockito.when(pacoteRepository.findByServicoId(1L)).thenReturn(list);

         Assertions.assertThrows(
                 EntityNotFoundException.class,
                 () -> servicoService.listarPacotesPorServico(1L));
      }
   }

   @Nested
   @DisplayName("ListarServicosPorProfissional")
   class ListarServicosPorProfissional {

      @Test
      @DisplayName("Deve retornar uma lista de serviços por profissional")
      void deveRetornarUmaListaDeServicosPorProfissional() {
         Servico servico = new Servico();
         servico.setId(1L);
         servico.setNome("Limpeza de pele");
         servico.setDescricao("Procedimento de remover impurezas, células mortas, cravos e miliuns da superfície do rosto.");
         servico.setFotoUrl("https://[NOME_DO_BUCKET].s3.[REGIÃO]://[CAMINHO/DA/IMAGEM.jpg]");
         servico.setDuracaoMinutos(40);
         servico.setPreco(120.00);
         servico.setAtivo(true);
         servico.setCriadoEm(LocalDateTime.now());

         List<Servico> list = new ArrayList<>();
         list.add(servico);

         Mockito.when(servicoRepository.findAllByProfissionalId(1L)).thenReturn(list);

         assertIterableEquals(list, servicoService.listarServicosPorProfissional(1L));
      }

      @Test
      @DisplayName("Deve retornar exception se nenhum serviço encontrado para o profissional")
      void deveRetornarExceptionSeListaVazia() {
         List<Servico> list = new ArrayList<>();

         Mockito.when(servicoRepository.findAllByProfissionalId(1L)).thenReturn(list);

         Assertions.assertThrows(
                 EntityNotFoundException.class,
                 () -> servicoService.listarServicosPorProfissional(1L));
      }
   }

   @Nested
   @DisplayName("Criar")
    class Criar {

      @Test
      @DisplayName("deve criar um servico com sucesso")
      void deveCriarUmServicoComSucesso(){
         Servico servico = new Servico();
         servico.setId(1L);
         servico.setNome("Limpeza de pele");
         servico.setDescricao("Procedimento de remover impurezas, células mortas, cravos e miliuns da superfície do rosto.");
         servico.setFotoUrl("https://[NOME_DO_BUCKET].s3.[REGIÃO]://[CAMINHO/DA/IMAGEM.jpg]");
         servico.setDuracaoMinutos(40);
         servico.setPreco(120.00);
         servico.setAtivo(true);
         servico.setCriadoEm(LocalDateTime.now());


         ServicoCadastroDto dto = new ServicoCadastroDto();
         servico.setNome("Limpeza de pele");
         servico.setDescricao("Procedimento de remover impurezas, células mortas, cravos e miliuns da superfície do rosto.");
         servico.setFotoUrl("https://[NOME_DO_BUCKET].s3.[REGIÃO]://[CAMINHO/DA/IMAGEM.jpg]");
         servico.setDuracaoMinutos(40);
         servico.setPreco(120.00);
         servico.setAtivo(true);
         servico.setCriadoEm(LocalDateTime.now());

         Mockito.when(servicoRepository.existsByNome(servico.getNome())).thenReturn(false);
         Mockito.when(servicoRepository.save(servico)).thenReturn(servico);

         assertEquals(servico,servicoService.cadastrar(dto));
      }


      @Test
      @DisplayName("Deve retornar uma exception se serviço existe por nome")
      void deveRetornarUmaExceptionSeServicoExistePorNome(){
         Servico servico = new Servico();
         servico.setId(1L);
         servico.setNome("Limpeza de pele");
         servico.setDescricao("Procedimento de remover impurezas, células mortas, cravos e miliuns da superfície do rosto.");
         servico.setFotoUrl("https://[NOME_DO_BUCKET].s3.[REGIÃO]://[CAMINHO/DA/IMAGEM.jpg]");
         servico.setDuracaoMinutos(40);
         servico.setPreco(120.00);
         servico.setAtivo(true);
         servico.setCriadoEm(LocalDateTime.now());


         ServicoCadastroDto dto = new ServicoCadastroDto();
         servico.setNome("Limpeza de pele");
         servico.setDescricao("Procedimento de remover impurezas, células mortas, cravos e miliuns da superfície do rosto.");
         servico.setFotoUrl("https://[NOME_DO_BUCKET].s3.[REGIÃO]://[CAMINHO/DA/IMAGEM.jpg]");
         servico.setDuracaoMinutos(40);
         servico.setPreco(120.00);
         servico.setAtivo(true);
         servico.setCriadoEm(LocalDateTime.now());



         Mockito.when(servicoRepository.existsByNome(servico.getNome())).thenReturn(true);

         Assertions.assertThrows(
                 EntityConflictException.class,
                 () ->  servicoService.cadastrar(dto));
      }
      }

      

   }

