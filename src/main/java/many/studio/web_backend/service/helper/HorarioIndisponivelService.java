package many.studio.web_backend.service.helper;

import many.studio.web_backend.dto.agendamento.HorarioIndisponivelDto;
import many.studio.web_backend.repository.AgendamentoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HorarioIndisponivelService {
    private AgendamentoRepository agendamentoRepository;

    public HorarioIndisponivelService(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

//    public HorarioIndisponivelService(){}
//
//    public List<HorarioIndisponivelDto> horariosIndisponiveis(String nomeServico){
//        List<HorarioIndisponivelDto> indisponiveis = new ArrayList<>();
//
//        indisponiveis.addAll(agendamentoRepository.buscarAgendamentosIndisponiveis(nomeServico));
//        indisponiveis.addAll(agendamentoRepository.buscarBloqueios(nomeServico));
//
//        return indisponiveis;
//    }
}