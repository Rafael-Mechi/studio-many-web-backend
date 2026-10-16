package many.studio.web_backend.service;

import many.studio.web_backend.dto.pacote.PacoteCadastroDto;
import many.studio.web_backend.dto.servico.CategoriaServicoCadastroDto;
import many.studio.web_backend.dto.servico.CategoriaServicoDto;
import many.studio.web_backend.dto.servico.ProfissionaisPorServicoDto;
import many.studio.web_backend.dto.servico.ServicoCadastroDto;
import many.studio.web_backend.dto.servico.ServicoListarDto;
import many.studio.web_backend.entity.CategoriaServico;
import many.studio.web_backend.entity.Pacote;
import many.studio.web_backend.entity.Profissional;
import many.studio.web_backend.entity.Servico;
import many.studio.web_backend.entity.ServicoProfissional;
import many.studio.web_backend.exception.BadRequestException;
import many.studio.web_backend.exception.EntityConflictException;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.mapper.PacoteMapper;
import many.studio.web_backend.mapper.ServicoMapper;
import many.studio.web_backend.repository.CategoriaServicoRepository;
import many.studio.web_backend.repository.PacoteRepository;
import many.studio.web_backend.repository.ProfissionalRepository;
import many.studio.web_backend.repository.ServicoProfissionalRepository;
import many.studio.web_backend.repository.ServicoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class ServicoService {

    private static final String NOME_PACOTE_AVULSO = "Sessão avulsa";
    private static final int VALIDADE_PACOTE_AVULSO_DIAS = 30;

    private final ServicoRepository servicoRepository;
    private final PacoteRepository pacoteRepository;
    private final ServicoProfissionalRepository servicoProfissionalRepository;
    private final ProfissionalRepository profissionalRepository;
    private final CategoriaServicoRepository categoriaServicoRepository;

    public ServicoService(PacoteRepository pacoteRepository, ServicoRepository servicoRepository, ServicoProfissionalRepository servicoProfissionalRepository, ProfissionalRepository profissionalRepository, CategoriaServicoRepository categoriaServicoRepository) {
        this.pacoteRepository = pacoteRepository;
        this.servicoRepository = servicoRepository;
        this.servicoProfissionalRepository = servicoProfissionalRepository;
        this.profissionalRepository = profissionalRepository;
        this.categoriaServicoRepository = categoriaServicoRepository;
    }


    public List<ServicoListarDto> listar(Boolean ativo){
        List<Servico> servicos = (ativo != null)
                ? servicoRepository.findByAtivo(ativo)
                : servicoRepository.findAll();
        List<ServicoListarDto> lista = new ArrayList<>();

        for(Servico s : servicos){
            lista.add(montarListarDto(s));
        }

        return lista;
    }

    public List<ServicoListarDto> listar(){
        return listar(null);
    }

    public List<Servico> listarServicosPorProfissional(Long id){
        if(!profissionalRepository.existsById(id)) {
            throw new EntityNotFoundException("Profissional não encontrado");
        }

        List<ServicoProfissional> servicoProfissionais = servicoProfissionalRepository.findAllByProfissionalId(id);

        List<Servico> servicos = new ArrayList<>();

        for (ServicoProfissional servicoProfissional : servicoProfissionais) {
            servicos.add(servicoProfissional.getServico());
        }

        return servicos;
    }

    @Transactional
    public ServicoListarDto criar(ServicoCadastroDto cadastroDto){
        if (servicoRepository.existsByNome(cadastroDto.getNome())){
            throw new EntityConflictException("já existe um serviço com esse nome");
        }

        Servico entity = ServicoMapper.toEntity(cadastroDto);
        entity.setCategoriaServico(buscarCategoria(cadastroDto.getCategoriaId()));
        entity.setCriadoEm(LocalDateTime.now());

        Servico salvo = servicoRepository.save(entity);

        sincronizarProfissionais(salvo, cadastroDto.getProfissionaisIds());
        garantirPacoteAvulso(salvo);

        return montarListarDto(salvo);
    }

    @Transactional
    public ServicoListarDto editar(Long id, ServicoCadastroDto dto) {
        Servico entity = buscarServico(id);

        servicoRepository.findByNome(dto.getNome()).ifPresent(outro -> {
            if (!outro.getId().equals(id)) {
                throw new EntityConflictException("já existe um serviço com esse nome");
            }
        });

        ServicoMapper.atualizarEntity(entity, dto);
        entity.setCategoriaServico(buscarCategoria(dto.getCategoriaId()));

        Servico salvo = servicoRepository.save(entity);

        sincronizarProfissionais(salvo, dto.getProfissionaisIds());
        garantirPacoteAvulso(salvo);

        return montarListarDto(salvo);
    }

    @Transactional
    public ServicoListarDto alterarStatus(Long id, Boolean ativo) {
        Servico entity = buscarServico(id);
        entity.setAtivo(ativo);
        return montarListarDto(servicoRepository.save(entity));
    }

    @Transactional
    public void deletar(Long id) {
        Servico servico = buscarServico(id);

        try {
            pacoteRepository.deleteAll(pacoteRepository.findByServicoId(id));
            servicoProfissionalRepository.deleteAll(servicoProfissionalRepository.findByServicoId(id));
            servicoRepository.delete(servico);
            servicoRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new EntityConflictException(
                    "Este serviço já possui agendamentos ou pacotes contratados e não pode ser excluído. Inative-o para que deixe de aparecer no agendamento.");
        }
    }


    public List<CategoriaServicoDto> listarCategorias() {
        return categoriaServicoRepository.findAll().stream()
                .map(c -> new CategoriaServicoDto(c.getId(), c.getCategoria()))
                .sorted((a, b) -> a.getCategoria().compareToIgnoreCase(b.getCategoria()))
                .toList();
    }

    @Transactional
    public CategoriaServicoDto criarCategoria(CategoriaServicoCadastroDto dto) {
        String nome = dto.getCategoria().trim();

        if (nome.isEmpty()) {
            throw new BadRequestException("Informe o nome da categoria");
        }

        if (categoriaServicoRepository.existsByCategoriaIgnoreCase(nome)) {
            throw new EntityConflictException("já existe uma categoria com esse nome");
        }

        CategoriaServico salva = new CategoriaServico();
        salva.setCategoria(nome);
        salva = categoriaServicoRepository.save(salva);

        return new CategoriaServicoDto(salva.getId(), salva.getCategoria());
    }


    public List<Pacote> listarPacotesPorServico(Long id){
        if(!servicoRepository.existsById(id)) {
            throw new EntityNotFoundException("Serviço não encontrado");
        }

        return pacoteRepository.findByServicoIdAndAtivoTrue(id);
    }

    public List<Pacote> listarTodosPacotesPorServico(Long id){
        if(!servicoRepository.existsById(id)) {
            throw new EntityNotFoundException("Serviço não encontrado");
        }

        return pacoteRepository.findByServicoId(id);
    }

    @Transactional
    public Pacote cadastrarPacote(Long servicoId, PacoteCadastroDto dto) {
        Servico servico = buscarServico(servicoId);

        if (pacoteRepository.existsByServicoIdAndNomeIgnoreCase(servicoId, dto.getNome().trim())) {
            throw new EntityConflictException("já existe um pacote com esse nome neste serviço");
        }

        validarPrecoDoPacote(servico, dto);

        return pacoteRepository.save(PacoteMapper.toEntity(dto, servico));
    }

    @Transactional
    public Pacote editarPacote(Long pacoteId, PacoteCadastroDto dto) {
        Pacote pacote = pacoteRepository.findById(pacoteId)
                .orElseThrow(() -> new EntityNotFoundException("Pacote não encontrado"));

        if (isPacoteAvulso(pacote)) {
            throw new EntityConflictException("A sessão avulsa acompanha o preço do serviço e não pode ser editada como pacote");
        }

        if (pacoteRepository.existsByServicoIdAndNomeIgnoreCaseAndIdNot(
                pacote.getServico().getId(), dto.getNome().trim(), pacoteId)) {
            throw new EntityConflictException("já existe um pacote com esse nome neste serviço");
        }

        validarPrecoDoPacote(pacote.getServico(), dto);

        PacoteMapper.atualizarEntity(pacote, dto);
        return pacoteRepository.save(pacote);
    }

    @Transactional
    public void deletarPacote(Long pacoteId) {
        Pacote pacote = pacoteRepository.findById(pacoteId)
                .orElseThrow(() -> new EntityNotFoundException("Pacote não encontrado"));

        if (isPacoteAvulso(pacote)) {
            throw new EntityConflictException("A sessão avulsa é necessária para o agendamento e não pode ser excluída");
        }

        try {
            pacoteRepository.delete(pacote);
            pacoteRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new EntityConflictException(
                    "Este pacote já foi contratado por clientes e não pode ser excluído. Desative-o para que deixe de ser oferecido.");
        }
    }


    private Servico buscarServico(Long id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado"));
    }

    private CategoriaServico buscarCategoria(Long categoriaId) {
        if (categoriaId == null) {
            return null;
        }
        return categoriaServicoRepository.findById(categoriaId.intValue())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
    }

    private ServicoListarDto montarListarDto(Servico s) {
        ServicoListarDto dto = ServicoMapper.toResponse(s);

        List<ProfissionaisPorServicoDto> profissionais = new ArrayList<>();
        for (ServicoProfissional sp : servicoProfissionalRepository.findByServicoId(s.getId())) {
            profissionais.add(new ProfissionaisPorServicoDto(
                    sp.getProfissional().getId(), sp.getProfissional().getNome()));
        }
        dto.setProfissionais(profissionais);

        return dto;
    }

    private void sincronizarProfissionais(Servico servico, List<Long> profissionaisIds) {
        Set<Long> desejados = profissionaisIds == null
                ? new LinkedHashSet<>()
                : new LinkedHashSet<>(profissionaisIds);

        List<ServicoProfissional> atuais = servicoProfissionalRepository.findByServicoId(servico.getId());
        Set<Long> jaVinculados = new LinkedHashSet<>();

        for (ServicoProfissional vinculo : atuais) {
            Long profissionalId = vinculo.getProfissional().getId();
            if (desejados.contains(profissionalId)) {
                jaVinculados.add(profissionalId);
            } else {
                servicoProfissionalRepository.delete(vinculo);
            }
        }

        for (Long profissionalId : desejados) {
            if (jaVinculados.contains(profissionalId)) {
                continue;
            }
            Profissional profissional = profissionalRepository.findById(profissionalId)
                    .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado: " + profissionalId));
            servicoProfissionalRepository.save(new ServicoProfissional(servico, profissional));
        }
    }

    private void garantirPacoteAvulso(Servico servico) {
        boolean existe = false;

        for (Pacote pacote : pacoteRepository.findByServicoId(servico.getId())) {
            if (isPacoteAvulso(pacote)) {
                existe = true;
                if (!servico.getPreco().equals(pacote.getPrecoTotal())) {
                    pacote.setPrecoTotal(servico.getPreco());
                    pacoteRepository.save(pacote);
                }
            }
        }

        if (!existe) {
            Pacote avulso = new Pacote();
            avulso.setNome(NOME_PACOTE_AVULSO);
            avulso.setTotalSessoes(1);
            avulso.setPrecoTotal(servico.getPreco());
            avulso.setValidadeDias(VALIDADE_PACOTE_AVULSO_DIAS);
            avulso.setAtivo(true);
            avulso.setCriadoEm(LocalDateTime.now());
            avulso.setServico(servico);
            pacoteRepository.save(avulso);
        }
    }

    private boolean isPacoteAvulso(Pacote pacote) {
        return pacote.getTotalSessoes() != null && pacote.getTotalSessoes() == 1;
    }

    private void validarPrecoDoPacote(Servico servico, PacoteCadastroDto dto) {
        Double precoOriginal = PacoteMapper.calcularPrecoOriginal(servico, dto.getTotalSessoes());

        if (precoOriginal != null && dto.getPrecoTotal() > precoOriginal) {
            throw new BadRequestException(
                    "O preço do pacote não pode ser maior que o valor das sessões avulsas (" + precoOriginal + ")");
        }
    }
}
