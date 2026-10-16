package many.studio.web_backend.mapper;

import many.studio.web_backend.dto.pacote.PacoteCadastroDto;
import many.studio.web_backend.dto.pacote.PacoteListarDto;
import many.studio.web_backend.dto.pacote.ServicoResumoDto;
import many.studio.web_backend.entity.Pacote;
import many.studio.web_backend.entity.Servico;

import java.time.LocalDateTime;
import java.util.List;

public class PacoteMapper {

    public static PacoteListarDto toResponse(Pacote pacote) {
        PacoteListarDto response = new PacoteListarDto();
        response.setId(pacote.getId());
        response.setAtivo(pacote.getAtivo());
        response.setNome(pacote.getNome());
        response.setPrecoTotal(pacote.getPrecoTotal());
        response.setTotalSessoes(pacote.getTotalSessoes());
        response.setValidadeDias(pacote.getValidadeDias());

        Servico servico = pacote.getServico();
        if (servico != null) {
            response.setServico(new ServicoResumoDto(servico.getId(), servico.getNome()));
        }

        Double precoOriginal = calcularPrecoOriginal(servico, pacote.getTotalSessoes());
        response.setPrecoOriginal(precoOriginal);
        response.setDescontoPercentual(calcularDesconto(precoOriginal, pacote.getPrecoTotal()));

        return response;
    }

    public static List<PacoteListarDto> toResponse(List<Pacote> list) {
        return list.stream().map(PacoteMapper::toResponse).toList();
    }

    public static Pacote toEntity(PacoteCadastroDto dto, Servico servico) {
        Pacote entity = new Pacote();
        entity.setCriadoEm(LocalDateTime.now());
        entity.setServico(servico);
        atualizarEntity(entity, dto);
        return entity;
    }

    public static void atualizarEntity(Pacote entity, PacoteCadastroDto dto) {
        entity.setNome(dto.getNome().trim());
        entity.setTotalSessoes(dto.getTotalSessoes());
        entity.setPrecoTotal(dto.getPrecoTotal());
        entity.setValidadeDias(dto.getValidadeDias());
        entity.setAtivo(dto.getAtivo());
    }

    public static Double calcularPrecoOriginal(Servico servico, Integer totalSessoes) {
        if (servico == null || servico.getPreco() == null || totalSessoes == null) {
            return null;
        }
        return arredondar(servico.getPreco() * totalSessoes);
    }

    public static Double calcularDesconto(Double precoOriginal, Double precoTotal) {
        if (precoOriginal == null || precoTotal == null || precoOriginal <= 0) {
            return 0.0;
        }
        double desconto = (1 - precoTotal / precoOriginal) * 100;
        return desconto <= 0 ? 0.0 : arredondar(desconto);
    }

    private static double arredondar(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
