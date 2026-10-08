package many.studio.web_backend.mapper.config;

import many.studio.web_backend.dto.config.HorarioDiaDto;
import many.studio.web_backend.dto.config.HorarioServicoDto;
import many.studio.web_backend.dto.config.HorariosConfigRequest;
import many.studio.web_backend.dto.config.IntervaloDto;
import many.studio.web_backend.entity.DiasDeTrabalho;
import many.studio.web_backend.entity.Profissional;
import many.studio.web_backend.entity.Servico;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class HorarioTrabalhoMapper {

    public static List<DayOfWeek> semanaOrdenada() {
        return List.of(
                DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY);
    }

    public static HorariosConfigRequest toResponse(List<DiasDeTrabalho> linhas, Profissional profissional) {
        Map<DayOfWeek, List<DiasDeTrabalho>> porDia = new EnumMap<>(DayOfWeek.class);
        for (DiasDeTrabalho linha : linhas) {
            porDia.computeIfAbsent(linha.getDiaDaSemana(), d -> new ArrayList<>()).add(linha);
        }

        HorariosConfigRequest response = new HorariosConfigRequest();
        if (profissional.getAlmocoInicio() != null && profissional.getAlmocoFim() != null) {
            response.setAlmoco(new IntervaloDto(profissional.getAlmocoInicio(), profissional.getAlmocoFim()));
        }

        List<HorarioDiaDto> dias = new ArrayList<>();
        for (DayOfWeek dia : semanaOrdenada()) {
            HorarioDiaDto diaDto = new HorarioDiaDto();
            diaDto.setDiaSemana(dia);

            List<DiasDeTrabalho> linhasDoDia = porDia.getOrDefault(dia, List.of());
            diaDto.setAtivo(!linhasDoDia.isEmpty());

            List<HorarioServicoDto> servicos = new ArrayList<>();
            for (DiasDeTrabalho linha : linhasDoDia) {
                servicos.add(toItemResponse(linha));
            }
            diaDto.setServicos(servicos);
            dias.add(diaDto);
        }
        response.setDias(dias);

        return response;
    }

    public static HorarioServicoDto toItemResponse(DiasDeTrabalho linha) {
        HorarioServicoDto item = new HorarioServicoDto();
        if (linha.getServico() != null) {
            item.setServicoId(linha.getServico().getId());
            item.setNome(linha.getServico().getNome());
        }
        item.setInicio(linha.getHoraInicio());
        item.setFim(linha.getHoraFim());
        return item;
    }

    public static DiasDeTrabalho toEntity(DayOfWeek diaSemana, HorarioServicoDto item,
                                           Profissional profissional, Servico servico) {
        DiasDeTrabalho linha = new DiasDeTrabalho();
        linha.setDiaDaSemana(diaSemana);
        linha.setHoraInicio(item.getInicio());
        linha.setHoraFim(item.getFim());
        linha.setProfissional(profissional);
        linha.setServico(servico);
        return linha;
    }
}
