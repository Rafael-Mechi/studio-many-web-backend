package many.studio.web_backend.service;

import many.studio.web_backend.dto.agendamento.HorarioIndisponivelDto;
import many.studio.web_backend.repository.AgendamentoRepository;
import many.studio.web_backend.service.helper.HorarioIndisponivelService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class HorarioIndisponivelServiceTest {
    @Mock
    private AgendamentoRepository agendamentoRepository;

    @InjectMocks
    private HorarioIndisponivelService service;

    @Test
    void deveRetornarHorariosIndisponiveis() {

        // GIVEN
        LocalDateTime inicio =
                LocalDateTime.of(2026, 5, 21, 14, 0);

        LocalDateTime fim =
                LocalDateTime.of(2026, 5, 21, 15, 0);

        HorarioIndisponivelDto dto =
                new HorarioIndisponivelDto(
                        inicio,
                        fim,
                        "Ana",
                        "AGENDAMENTO"
                );

        Mockito.when(
                agendamentoRepository
                        .buscarAgendamentosIndisponiveis(
                                "Limpeza de Pele"
                        )
        ).thenReturn(List.of(dto));

        Mockito.when(
                agendamentoRepository
                        .buscarBloqueios(
                                "Limpeza de Pele"
                        )
        ).thenReturn(List.of());

        // WHEN
        List<HorarioIndisponivelDto> resultado =
                service.horariosIndisponiveis(
                        "Limpeza de Pele"
                );

        // THEN
        Assertions.assertEquals(1, resultado.size());
        Assertions.assertEquals("Ana", resultado.get(0).getNomeProfissional());
    }
}