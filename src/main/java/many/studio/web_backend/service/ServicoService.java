package many.studio.web_backend.service;

import many.studio.web_backend.dto.pacote.PacoteListarDto;
import many.studio.web_backend.dto.servico.ServicoListarDto;
import many.studio.web_backend.entity.Servico;
import many.studio.web_backend.mapper.PacoteMapper;
import many.studio.web_backend.mapper.ServicoMapper;
import many.studio.web_backend.repository.PacoteRepository;
import many.studio.web_backend.repository.ServicoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;
    private final PacoteRepository pacoteRepository;

    public ServicoService(PacoteRepository pacoteRepository, ServicoRepository servicoRepository) {
        this.pacoteRepository = pacoteRepository;
        this.servicoRepository = servicoRepository;
    }

    public List<ServicoListarDto> listar(){
        return  ServicoMapper.toResponse(servicoRepository.findAll());
    }

    public List<PacoteListarDto> listarPacotesPorServico(Long id){
        return PacoteMapper.toResponse(pacoteRepository.findByServicoId(id));
    }

}
