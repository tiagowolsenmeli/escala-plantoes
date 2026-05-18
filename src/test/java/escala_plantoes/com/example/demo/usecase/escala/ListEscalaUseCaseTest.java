package escala_plantoes.com.example.demo.usecase.escala;

import escala_plantoes.com.example.demo.controller.escala.dto.EscalaResponseDTO;
import escala_plantoes.com.example.demo.domain.plantao.Plantao;
import escala_plantoes.com.example.demo.domain.plantao.Turno;
import escala_plantoes.com.example.demo.domain.professional.Professional;
import escala_plantoes.com.example.demo.domain.professional.ProfessionalRegistration;
import escala_plantoes.com.example.demo.service.plantao.PlantaoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListEscalaUseCaseTest {

    @Mock
    private PlantaoService plantaoService;

    @InjectMocks
    private ListEscalaUseCase useCase;

    private static final LocalDate START = LocalDate.of(2026, 6, 1);

    private Professional professional(long id, String name, String category, String registrationNumber) {
        ProfessionalRegistration reg = new ProfessionalRegistration();
        reg.setCategory(category);
        reg.setRegistrationNumber(registrationNumber);

        Professional p = new Professional();
        p.setId(id);
        p.setName(name);
        p.setWorkSchedule(40);
        p.setRegistration(reg);
        return p;
    }

    private Plantao plantao(long id, Professional professional, LocalDate data, Turno turno) {
        Plantao p = new Plantao();
        p.setId(id);
        p.setProfessional(professional);
        p.setData(data);
        p.setTurno(turno);
        return p;
    }

    @Test
    void execute_returnsEmpty_whenNoPlantoesInPeriod() {
        when(plantaoService.listByPeriod(any(), any())).thenReturn(List.of());

        List<EscalaResponseDTO> result = useCase.execute(START);

        assertTrue(result.isEmpty());
    }

    @Test
    void execute_queriesWindowOf7Days() {
        when(plantaoService.listByPeriod(any(), any())).thenReturn(List.of());

        useCase.execute(START);

        verify(plantaoService).listByPeriod(START, START.plusDays(6));
    }

    @Test
    void execute_returnsDtoWithCorrectProfessionalFields() {
        Professional p = professional(1L, "Dr. João Silva", "MÉDICO", "123456");
        when(plantaoService.listByPeriod(any(), any()))
                .thenReturn(List.of(plantao(10L, p, START, Turno.MANHA)));

        List<EscalaResponseDTO> result = useCase.execute(START);

        assertEquals(1, result.size());
        EscalaResponseDTO dto = result.get(0);
        assertEquals(1L, dto.professionalId());
        assertEquals("Dr. João Silva", dto.professionalName());
        assertEquals("MÉDICO", dto.professionalCategory());
        assertEquals("123456", dto.professionalRegistrationNumber());
    }

    @Test
    void execute_mapsPlantaoItemCorrectly() {
        Professional p = professional(1L, "Dr. João Silva", "MÉDICO", "123456");
        when(plantaoService.listByPeriod(any(), any()))
                .thenReturn(List.of(plantao(10L, p, START.plusDays(2), Turno.NOITE)));

        EscalaResponseDTO dto = useCase.execute(START).get(0);

        assertEquals(1, dto.plantoes().size());
        var item = dto.plantoes().get(0);
        assertEquals(10L, item.id());
        assertEquals(START.plusDays(2), item.data());
        assertEquals(Turno.NOITE, item.turno());
    }

    @Test
    void execute_groupsMultiplePlantoesBySameProfessional() {
        Professional p = professional(1L, "Dr. João Silva", "MÉDICO", "123456");
        when(plantaoService.listByPeriod(any(), any()))
                .thenReturn(List.of(
                        plantao(10L, p, START,              Turno.MANHA),
                        plantao(11L, p, START.plusDays(1),  Turno.TARDE),
                        plantao(12L, p, START.plusDays(2),  Turno.NOITE)
                ));

        List<EscalaResponseDTO> result = useCase.execute(START);

        assertEquals(1, result.size());
        assertEquals(3, result.get(0).plantoes().size());
    }

    @Test
    void execute_returnsOneDtoPerProfessional() {
        Professional p1 = professional(1L, "Dr. João Silva",   "MÉDICO",     "111111");
        Professional p2 = professional(2L, "Enf. Maria Souza", "ENFERMEIRO", "222222");
        when(plantaoService.listByPeriod(any(), any()))
                .thenReturn(List.of(
                        plantao(10L, p1, START,             Turno.MANHA),
                        plantao(11L, p2, START.plusDays(1), Turno.TARDE)
                ));

        List<EscalaResponseDTO> result = useCase.execute(START);

        assertEquals(2, result.size());
    }
}
