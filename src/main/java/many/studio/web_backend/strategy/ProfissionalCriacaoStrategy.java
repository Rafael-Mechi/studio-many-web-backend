package many.studio.web_backend.strategy;

import many.studio.web_backend.dto.usuario.UsuarioCriacaoDto;
import many.studio.web_backend.entity.Profissional;
import many.studio.web_backend.entity.Servico;
import many.studio.web_backend.entity.ServicoProfissional;
import many.studio.web_backend.entity.Usuario;
import many.studio.web_backend.exception.EntityNotFoundException;
import many.studio.web_backend.repository.ProfissionalRepository;
import many.studio.web_backend.repository.ServicoProfissionalRepository;
import many.studio.web_backend.repository.ServicoRepository;
import org.springframework.stereotype.Component;

@Component
public class ProfissionalCriacaoStrategy implements UsuarioCriacaoStrategy {

    private final ProfissionalRepository profissionalRepository;
    private final ServicoRepository servicoRepository;
    private final ServicoProfissionalRepository servicoProfissionalRepository;

    public ProfissionalCriacaoStrategy(ProfissionalRepository profissionalRepository, ServicoRepository servicoRepository, ServicoProfissionalRepository servicoProfissionalRepository) {
        this.profissionalRepository = profissionalRepository;
        this.servicoRepository = servicoRepository;
        this.servicoProfissionalRepository = servicoProfissionalRepository;
    }

    @Override
    public boolean suporta(Long perfilId) {
        return perfilId == 1L || perfilId == 2L;
    }

    @Override
    public void criarDadosComplementares(Usuario usuario, UsuarioCriacaoDto dto) {
        Profissional profissional = new Profissional();
        profissional.setNome(dto.getNome());
        profissional.setUsuario(usuario);

        profissionalRepository.save(profissional);

        for (String nomeServico : dto.getServicos()){
            Servico servico = servicoRepository.findByNome(nomeServico)
                    .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado"));

            ServicoProfissional profissionalServico =
                    new ServicoProfissional(servico, profissional);

            servicoProfissionalRepository.save(profissionalServico);
        }
    }
}