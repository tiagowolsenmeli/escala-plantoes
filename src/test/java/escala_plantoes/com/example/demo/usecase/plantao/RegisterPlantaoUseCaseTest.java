package escala_plantoes.com.example.demo.usecase.plantao;

import escala_plantoes.com.example.demo.controller.plantao.dto.PlantaoRequestDTO;
import escala_plantoes.com.example.demo.controller.plantao.dto.PlantaoResponseDTO;
import escala_plantoes.com.example.demo.domain.plantao.CargaHorariaExceededException;
import escala_plantoes.com.example.demo.domain.plantao.DuplicatePlantaoException;
import escala_plantoes.com.example.demo.domain.plantao.Plantao;
import escala_plantoes.com.example.demo.domain.plantao.Turno;
import escala_plantoes.com.example.demo.domain.professional.Professional;
import escala_plantoes.com.example.demo.domain.professional.ProfessionalRegistration;
import escala_plantoes.com.example.demo.service.plantao.PlantaoService;
import escala_plantoes.com.example.demo.service.professional.ProfessionalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterPlantaoUseCaseTest {

    @Mock
    private PlantaoService plantaoService;

    @Mock
    private ProfessionalService professionalService;

    @InjectMocks
    private RegisterPlantaoUseCase useCase;

    private static final LocalDate DATE = LocalDate.now().plusDays(1);
    private static final Long PROFESSIONAL_ID = 1L;

    private Professional professional;

    @BeforeEach
    void setUp() {
        ProfessionalRegistration reg = new ProfessionalRegistration();
        reg.setCategory("MÉDICO");
        reg.setRegistrationNumber("12345");

        professional = new Professional();
        professional.setId(PROFESSIONAL_ID);
        professional.setName("Dr. Test");
        professional.setWorkSchedule(40);
        professional.setRegistration(reg);
    }

    private Plantao plantaoWith(Turno turno, LocalDate data) {
        Plantao p = new Plantao();
        p.setTurno(turno);
        p.setData(data);
        return p;
    }

    private Plantao savedPlantao(Turno turno, LocalDate data) {
        Plantao p = plantaoWith(turno, data);
        p.setId(10L);
        p.setProfessional(professional);
        return p;
    }

    @Test
    void execute_returnsDto_whenValid() {
        PlantaoRequestDTO dto = new PlantaoRequestDTO(PROFESSIONAL_ID, DATE, Turno.MANHA);

        when(professionalService.findById(PROFESSIONAL_ID)).thenReturn(professional);
        when(plantaoService.existsByProfessionalAndTurnoAndData(PROFESSIONAL_ID, Turno.MANHA, DATE)).thenReturn(false);
        when(plantaoService.listByProfessionalAndPeriod(eq(PROFESSIONAL_ID), any(), any())).thenReturn(List.of());
        when(plantaoService.register(any())).thenReturn(savedPlantao(Turno.MANHA, DATE));

        PlantaoResponseDTO result = useCase.execute(dto);

        assertEquals(10L, result.id());
        assertEquals(PROFESSIONAL_ID, result.professionalId());
        assertEquals("Dr. Test", result.professionalName());
        assertEquals(Turno.MANHA, result.turno());
        assertEquals(DATE, result.data());
    }

    @Test
    void execute_savesPlantaoWithCorrectFields() {
        PlantaoRequestDTO dto = new PlantaoRequestDTO(PROFESSIONAL_ID, DATE, Turno.TARDE);

        when(professionalService.findById(PROFESSIONAL_ID)).thenReturn(professional);
        when(plantaoService.existsByProfessionalAndTurnoAndData(any(), any(), any())).thenReturn(false);
        when(plantaoService.listByProfessionalAndPeriod(any(), any(), any())).thenReturn(List.of());
        when(plantaoService.register(any())).thenReturn(savedPlantao(Turno.TARDE, DATE));

        useCase.execute(dto);

        ArgumentCaptor<Plantao> captor = ArgumentCaptor.forClass(Plantao.class);
        verify(plantaoService).register(captor.capture());
        Plantao saved = captor.getValue();
        assertEquals(professional, saved.getProfessional());
        assertEquals(DATE, saved.getData());
        assertEquals(Turno.TARDE, saved.getTurno());
    }

    @Test
    void execute_throwsDuplicatePlantaoException_whenDuplicate() {
        PlantaoRequestDTO dto = new PlantaoRequestDTO(PROFESSIONAL_ID, DATE, Turno.TARDE);

        when(professionalService.findById(PROFESSIONAL_ID)).thenReturn(professional);
        when(plantaoService.existsByProfessionalAndTurnoAndData(PROFESSIONAL_ID, Turno.TARDE, DATE)).thenReturn(true);

        assertThrows(DuplicatePlantaoException.class, () -> useCase.execute(dto));
        verify(plantaoService, never()).register(any());
    }

    @Test
    void execute_throwsCargaHorariaExceededException_whenExceeded() {
        professional.setWorkSchedule(20);
        // 12h existente na janela + 12h (NOITE) = 24h > 20h
        PlantaoRequestDTO dto = new PlantaoRequestDTO(PROFESSIONAL_ID, DATE, Turno.NOITE);

        when(professionalService.findById(PROFESSIONAL_ID)).thenReturn(professional);
        when(plantaoService.existsByProfessionalAndTurnoAndData(any(), any(), any())).thenReturn(false);
        when(plantaoService.listByProfessionalAndPeriod(eq(PROFESSIONAL_ID), any(), any()))
                .thenReturn(List.of(plantaoWith(Turno.NOITE, DATE.minusDays(1))));

        assertThrows(CargaHorariaExceededException.class, () -> useCase.execute(dto));
        verify(plantaoService, never()).register(any());
    }

    @Test
    void execute_succeeds_whenHorasExactlyAtLimit() {
        professional.setWorkSchedule(30);
        // 24h existentes na janela + 6h (MANHA) = 30h = limite exato
        PlantaoRequestDTO dto = new PlantaoRequestDTO(PROFESSIONAL_ID, DATE, Turno.MANHA);

        when(professionalService.findById(PROFESSIONAL_ID)).thenReturn(professional);
        when(plantaoService.existsByProfessionalAndTurnoAndData(any(), any(), any())).thenReturn(false);
        when(plantaoService.listByProfessionalAndPeriod(eq(PROFESSIONAL_ID), any(), any()))
                .thenReturn(List.of(
                        plantaoWith(Turno.NOITE, DATE.minusDays(1)), // 12h
                        plantaoWith(Turno.NOITE, DATE.minusDays(2))  // 12h
                ));
        when(plantaoService.register(any())).thenReturn(savedPlantao(Turno.MANHA, DATE));

        assertDoesNotThrow(() -> useCase.execute(dto));
    }

    @Test
    void execute_queriesWindowOf3DaysAroundTarget() {
        PlantaoRequestDTO dto = new PlantaoRequestDTO(PROFESSIONAL_ID, DATE, Turno.MANHA);

        when(professionalService.findById(PROFESSIONAL_ID)).thenReturn(professional);
        when(plantaoService.existsByProfessionalAndTurnoAndData(any(), any(), any())).thenReturn(false);
        when(plantaoService.listByProfessionalAndPeriod(any(), any(), any())).thenReturn(List.of());
        when(plantaoService.register(any())).thenReturn(savedPlantao(Turno.MANHA, DATE));

        useCase.execute(dto);

        verify(plantaoService).listByProfessionalAndPeriod(PROFESSIONAL_ID, DATE.minusDays(3), DATE.plusDays(3));
    }
}
