package many.studio.web_backend.mapper.config;

import many.studio.web_backend.dto.usuario.MeuPerfilClienteResponse;
import many.studio.web_backend.dto.usuario.MeuPerfilProfissionalResponse;
import many.studio.web_backend.entity.Cliente;
import many.studio.web_backend.entity.Profissional;
import many.studio.web_backend.entity.Usuario;

public class MeuPerfilMapper {

    public static MeuPerfilProfissionalResponse toProfissionalResponse(Usuario usuario,
                                                                       Profissional profissional,
                                                                       String role,
                                                                       int totalAgendamentos,
                                                                       int totalClientes) {
        MeuPerfilProfissionalResponse dto = new MeuPerfilProfissionalResponse();
        dto.setId(usuario.getId());
        dto.setNome(profissional.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setTelefone(profissional.getTelefone());
        dto.setCpf(profissional.getDocumento());
        dto.setAtivo(usuario.getAtivo());
        if (usuario.getCriadoEm() != null) {
            dto.setMembroDesde(usuario.getCriadoEm().toLocalDate());
        }
        dto.setRole(role);
        dto.setStats(new MeuPerfilProfissionalResponse.Stats(totalAgendamentos, totalClientes));
        return dto;
    }

    public static MeuPerfilClienteResponse toClienteResponse(Usuario usuario,
                                                              Cliente cliente,
                                                              String role,
                                                              int totalAgendamentos,
                                                              int totalFichas) {
        MeuPerfilClienteResponse dto = new MeuPerfilClienteResponse();
        dto.setId(usuario.getId());
        dto.setClienteId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setTelefone(cliente.getTelefone());
        dto.setClienteTelefone(cliente.getTelefone());
        dto.setCpf(cliente.getDocumento());
        dto.setAtivo(usuario.getAtivo());
        if (usuario.getCriadoEm() != null) {
            dto.setMembroDesde(usuario.getCriadoEm().toLocalDate());
        }
        dto.setRole(role);
        dto.setStats(new MeuPerfilClienteResponse.Stats(totalAgendamentos, totalFichas));
        return dto;
    }
}
