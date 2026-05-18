package escala_plantoes.com.example.demo.usecase.professional;

import escala_plantoes.com.example.demo.controller.professional.dto.ProfessionalResponseDTO;
import escala_plantoes.com.example.demo.domain.professional.Professional;
import escala_plantoes.com.example.demo.domain.professional.ProfessionalRegistration;
import escala_plantoes.com.example.demo.service.professional.ProfessionalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListProfessionalsUseCaseTest {

    @Mock
    private ProfessionalService professionalService;

    @InjectMocks
    private ListProfessionalsUseCase useCase;

    private Professional professional(long id, String name, int workSchedule, String category, String registrationNumber) {
        ProfessionalRegistration reg = new ProfessionalRegistration();
        reg.setCategory(category);
        reg.setState("SP");
        reg.setType("CRM");
        reg.setRegistrationNumber(registrationNumber);

        Professional p = new Professional();
        p.setId(id);
        p.setName(name);
        p.setWorkSchedule(workSchedule);
        p.setRegistration(reg);
        return p;
    }

    @Test
    void execute_returnsEmpty_whenNoProfessionals() {
        when(professionalService.list()).thenReturn(List.of());

        assertTrue(useCase.execute().isEmpty());
    }

    @Test
    void execute_returnsDtoWithCorrectFields() {
        when(professionalService.list()).thenReturn(
                List.of(professional(1L, "Dr. João Silva", 40, "MÉDICO", "12345")));

        List<ProfessionalResponseDTO> result = useCase.execute();

        assertEquals(1, result.size());
        ProfessionalResponseDTO dto = result.get(0);
        assertEquals(1L, dto.id());
        assertEquals("Dr. João Silva", dto.name());
        assertEquals(40, dto.workSchedule());
        assertEquals("MÉDICO", dto.registration().category());
        assertEquals("12345", dto.registration().registrationNumber());
    }

    @Test
    void execute_returnsOneDtoPerProfessional() {
        when(professionalService.list()).thenReturn(List.of(
                professional(1L, "Dr. João Silva",   40, "MÉDICO",     "11111"),
                professional(2L, "Enf. Maria Souza", 30, "ENFERMEIRO", "22222"),
                professional(3L, "Téc. Ana Costa",   20, "TÉCNICO",    "33333")
        ));

        assertEquals(3, useCase.execute().size());
    }
}
