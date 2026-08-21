package many.studio.web_backend.service;

import many.studio.web_backend.dto.pacote.PacoteCadastroDto;
import many.studio.web_backend.dto.pacote.PacoteListarDto;
import many.studio.web_backend.dto.servico.ProfissionaisPorServicoDto;
import many.studio.web_backend.dto.servico.ServicoCadastroDto;
import many.studio.web_backend.dto.servico.ServicoListarDto;
import many.studio.web_backend.entity.Pacote;
import many.studio.web_backend.entity.Profissional;
import many.studio.web_backend.entity.Servico;
import many.studio.web_backend.entity.ServicoProfissional;
import many.studio.web_backend.exception.EntityConflictException;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.mapper.PacoteMapper;
import many.studio.web_backend.mapper.ServicoMapper;
import many.studio.web_backend.repository.PacoteRepository;
import many.studio.web_backend.repository.ServicoProfissionalRepository;
import many.studio.web_backend.repository.ServicoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;
    private final PacoteRepository pacoteRepository;
    private final ServicoProfissionalRepository servicoProfissionalRepository;

    public ServicoService(PacoteRepository pacoteRepository, ServicoRepository servicoRepository, ServicoProfissionalRepository servicoProfissionalRepository) {
        this.pacoteRepository = pacoteRepository;
        this.servicoRepository = servicoRepository;
        this.servicoProfissionalRepository = servicoProfissionalRepository;
    }

    public List<ServicoListarDto> listar(){
        List<Servico> servicos = servicoRepository.findAll();
        List<ServicoListarDto> lista = new ArrayList<>();

        if (servicos.isEmpty()) {
            throw new EntityNotFoundException("Nenhum serviço encontrado");
        }

        for(Servico s : servicos){
            ServicoListarDto servicoListarDto = new ServicoListarDto();
            servicoListarDto.setId(s.getId());
            servicoListarDto.setNome(s.getNome());
            servicoListarDto.setDescricao(s.getDescricao());
            servicoListarDto.setFotoUrl(s.getFotoUrl());
            servicoListarDto.setDuracaoMinutos(s.getDuracaoMinutos());
            servicoListarDto.setPreco(s.getPreco());
            servicoListarDto.setSinalValor(s.getSinalValor());
            servicoListarDto.setAtivo(s.getAtivo());
            servicoListarDto.setCategoria(s.getCategoriaServico().getCategoria());
            servicoListarDto.setCriadoEm(s.getCriadoEm());

            List<ServicoProfissional> servicosProfissionais = servicoProfissionalRepository.findByServicoId(s.getId());
            List<ProfissionaisPorServicoDto> profissionaisPorServicoDto = new ArrayList<>();
            for(ServicoProfissional sp : servicosProfissionais){
                ProfissionaisPorServicoDto profissionalPorServico = new ProfissionaisPorServicoDto();
                profissionalPorServico.setId(sp.getProfissional().getId());
                profissionalPorServico.setNomeFuncionario(sp.getProfissional().getNome());

                profissionaisPorServicoDto.add(profissionalPorServico);
            }

            servicoListarDto.setProfissionais(profissionaisPorServicoDto);
            lista.add(servicoListarDto);
        }

        return lista;
    }

    public List<Pacote> listarPacotesPorServico(Long id){
        List<Pacote> pacotes = pacoteRepository.findByServicoId(id);

        if (pacotes.isEmpty()) {
            throw new EntityNotFoundException("Nenhum pacote encontrado para o serviço informado");
        }

        return pacotes;
    }


    public List<Servico> listarServicosPorProfissional(Long id){
        List<ServicoProfissional> servicoProfissionals = servicoProfissionalRepository.findAllByProfissionalId(id);

        if (servicoProfissionals.isEmpty()) {
            throw new EntityNotFoundException("Nenhum serviço encontrado para o profissional informado");
        }

        List<Servico> servicos = new ArrayList<>();

        for (ServicoProfissional servicoProfissional : servicoProfissionals) {
            servicos.add(servicoProfissional.getServico());
        }

        return servicos;
    }

    public Servico criar(ServicoCadastroDto cadastroDto){
        if (servicoRepository.existsByNome(cadastroDto.getNome())){
            throw new EntityConflictException("já existe um serviço com esse nome");
        }
        Servico entity = ServicoMapper.toEntity(cadastroDto);
        return servicoRepository.save(entity);
    }

    public Servico editar(Long id, ServicoCadastroDto dto) {
        if (!servicoRepository.existsById(id)){
            throw new EntityNotFoundException("Serviço não encontrado");
        }
        Servico entity = ServicoMapper.toEntity(dto);

        return servicoRepository.save(entity);
    }

    public void deletar(Long id) {
        if (!servicoRepository.existsById(id)){
            throw new EntityNotFoundException("Serviço não encontrado");
        }
        servicoRepository.deleteById(id);
    }

    public  Pacote editarPacote(Long pacoteId, PacoteCadastroDto dto) {
        if (!pacoteRepository.existsById(pacoteId)) {
            throw new EntityNotFoundException("Pacote não encontrado");
        }
        Pacote pacote = PacoteMapper.toEntity(dto);
        pacote.setId(pacoteId);
        return pacoteRepository.save(pacote);
    }

    public  void deletarPacote(Long pacoteId) {
        if (!pacoteRepository.existsById(pacoteId)) {
            throw new EntityNotFoundException("Pacote não encontrado");
        }
        pacoteRepository.deleteById(pacoteId);
    }

    public  Pacote cadastrarPacote(PacoteCadastroDto dto) {
        return pacoteRepository.save(PacoteMapper.toEntity(dto));
    }
}
