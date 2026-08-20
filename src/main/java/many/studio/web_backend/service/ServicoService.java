package many.studio.web_backend.service;

import many.studio.web_backend.dto.pacote.PacoteCadastroDto;
import many.studio.web_backend.dto.pacote.PacoteListarDto;
import many.studio.web_backend.dto.servico.ServicoCadastroDto;
import many.studio.web_backend.dto.servico.ServicoListarDto;
import many.studio.web_backend.entity.Pacote;
import many.studio.web_backend.entity.Servico;
import many.studio.web_backend.entity.ServicoProfissional;
import many.studio.web_backend.exception.EntityConflictException;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.mapper.PacoteMapper;
import many.studio.web_backend.mapper.ServicoMapper;
import many.studio.web_backend.repository.PacoteRepository;
import many.studio.web_backend.repository.ProfissionalRepository;
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
    private final ProfissionalRepository profissionalRepository;

    public ServicoService(PacoteRepository pacoteRepository, ServicoRepository servicoRepository, ServicoProfissionalRepository servicoProfissionalRepository, ProfissionalRepository profissionalRepository) {
        this.pacoteRepository = pacoteRepository;
        this.servicoRepository = servicoRepository;
        this.servicoProfissionalRepository = servicoProfissionalRepository;
        this.profissionalRepository = profissionalRepository;
    }

    public List<Servico> listar(){
        return servicoRepository.findAll();
    }

    public List<Pacote> listarPacotesPorServico(Long id){

        if(!servicoRepository.existsById(id)) {
            throw new EntityNotFoundException("Serviço não encontrado");
        }

        return pacoteRepository.findByServicoId(id);
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
