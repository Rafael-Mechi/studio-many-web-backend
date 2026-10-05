package many.studio.web_backend.service;

import many.studio.web_backend.dto.anamnese.AnamneseListResponse;
import many.studio.web_backend.dto.anamnese.AnamneseRequest;
import many.studio.web_backend.dto.anamnese.AnamneseResponse;
import many.studio.web_backend.entity.Anamnese;
import many.studio.web_backend.entity.Cliente;
import many.studio.web_backend.entity.Profissional;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.mapper.AnamneseMapper;
import many.studio.web_backend.repository.AnamneseRepository;
import many.studio.web_backend.repository.ClienteRepository;
import many.studio.web_backend.repository.ProfissionalRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AnamneseService {
    private final AnamneseRepository anamneseRepository;
    private final ProfissionalRepository profissionalRepository;
    private final ClienteRepository clienteRepository;

    public AnamneseService(AnamneseRepository anamneseRepository, ProfissionalRepository profissionalRepository, ClienteRepository clienteRepository) {
        this.anamneseRepository = anamneseRepository;
        this.profissionalRepository = profissionalRepository;
        this.clienteRepository = clienteRepository;
    }

    public AnamneseResponse criarFicha(Long usuarioId, AnamneseRequest anamneseRequest){
        Profissional profissional = profissionalRepository
                .findByUsuario_Id(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));

        Cliente cliente = clienteRepository
                .findById(anamneseRequest.getCliente())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        Anamnese anamnese = new Anamnese();
        anamnese.setProfissional(profissional);
        anamnese.setCliente(cliente);
        anamnese.setInformacao(anamneseRequest.getInformacao());

        return AnamneseMapper.toResponse(anamneseRepository.save(anamnese));
    }

    public Page<AnamneseListResponse> listarFichas(Long usuarioId, String role, Pageable pageable){
        if(role.equals("ROLE_ADMIN")){
            return anamneseRepository.findAll(pageable)
                    .map(AnamneseMapper::toListResponse);
        } else if(role.equals("ROLE_PROFISSIONAL")){
            Profissional profissional = profissionalRepository
                    .findByUsuario_Id(usuarioId)
                    .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));

            return anamneseRepository.findByProfissional(profissional, pageable)
                    .map(AnamneseMapper::toListResponse);
        } else {
            throw new EntityNotFoundException("Perfil não autorizado");
        }
    }
}
