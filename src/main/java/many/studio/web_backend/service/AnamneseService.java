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

    public Page<AnamneseListResponse> listarFichas(
            Long usuarioId,
            String role,
            String busca,
            Pageable pageable
    ) {
        boolean possuiBusca = busca != null && !busca.isBlank();

        if (role.equals("ROLE_ADMIN")) {

            Page<Anamnese> fichas = possuiBusca
                    ? anamneseRepository.buscarPorNomeOuTelefone(busca, pageable)
                    : anamneseRepository.findAll(pageable);

            return fichas.map(AnamneseMapper::toListResponse);

        } else if (role.equals("ROLE_PROFISSIONAL")) {

            Profissional profissional = profissionalRepository
                    .findByUsuario_Id(usuarioId)
                    .orElseThrow(() ->
                            new EntityNotFoundException("Profissional não encontrado")
                    );

            Page<Anamnese> fichas = possuiBusca
                    ? anamneseRepository.buscarPorProfissionalNomeOuTelefone(
                    profissional,
                    busca,
                    pageable
            )
                    : anamneseRepository.findByProfissional(
                    profissional,
                    pageable
            );

            return fichas.map(AnamneseMapper::toListResponse);
        }

        throw new EntityNotFoundException("Perfil não autorizado");
    }

    public AnamneseResponse buscarFichaPorId(Long id, Long usuarioId, String role) {
        Anamnese anamnese = anamneseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ficha não encontrada"));

        if (role.equals("ROLE_PROFISSIONAL")) {
            Profissional profissional = profissionalRepository
                    .findByUsuario_Id(usuarioId)
                    .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));

            if (!anamnese.getProfissional().getId().equals(profissional.getId())) {
                throw new EntityNotFoundException("Ficha não encontrada");
            }
        }

        return AnamneseMapper.toResponse(anamnese);
    }

    public AnamneseResponse atualizarFicha(Long id, AnamneseRequest anamneseRequest, Long usuarioId, String role) {
        Anamnese anamnese = anamneseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ficha não encontrada"));

        if (role.equals("ROLE_PROFISSIONAL")) {
            Profissional profissional = profissionalRepository
                    .findByUsuario_Id(usuarioId)
                    .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));

            if (!anamnese.getProfissional().getId().equals(profissional.getId())) {
                throw new EntityNotFoundException("Ficha não encontrada");
            }
        }

        if (anamneseRequest.getCliente() != null) {
            Cliente cliente = clienteRepository
                    .findById(anamneseRequest.getCliente())
                    .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
            anamnese.setCliente(cliente);
        }

        if (anamneseRequest.getInformacao() != null) {
            anamnese.setInformacao(anamneseRequest.getInformacao());
        }

        return AnamneseMapper.toResponse(anamneseRepository.save(anamnese));
    }

    public void excluirFicha(Long id, Long usuarioId, String role) {
        Anamnese anamnese = anamneseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ficha não encontrada"));

        if (role.equals("ROLE_PROFISSIONAL")) {
            Profissional profissional = profissionalRepository
                    .findByUsuario_Id(usuarioId)
                    .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));

            if (!anamnese.getProfissional().getId().equals(profissional.getId())) {
                throw new EntityNotFoundException("Ficha não encontrada");
            }
        }

        anamneseRepository.delete(anamnese);
    }

    public AnamneseResponse buscarFichaPorCliente(Long clienteId, Long usuarioId, String role) {
        Profissional profissional = null;

        if (role.equals("ROLE_PROFISSIONAL")) {
            profissional = profissionalRepository
                    .findByUsuario_Id(usuarioId)
                    .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));
        }

        Anamnese anamnese;
        if (role.equals("ROLE_ADMIN")) {
            anamnese = anamneseRepository.findTopByClienteIdOrderByIdDesc(clienteId)
                    .orElseThrow(() -> new EntityNotFoundException("Ficha não encontrada para este cliente"));
        } else if (role.equals("ROLE_PROFISSIONAL")) {
            anamnese = anamneseRepository.findTopByClienteIdAndProfissionalOrderByIdDesc(clienteId, profissional)
                    .orElseThrow(() -> new EntityNotFoundException("Ficha não encontrada para este cliente"));
        } else {
            throw new EntityNotFoundException("Recurso não encontrado");
        }

        return AnamneseMapper.toResponse(anamnese);
    }
}
