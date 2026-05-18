package escala_plantoes.com.example.demo.usecase.professional;

import escala_plantoes.com.example.demo.controller.professional.dto.ProfessionalFilterRequestDTO;
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
class ListProfessionalsByCategoryUseCaseTest {

    @Mock
    private ProfessionalService professionalService;

    @InjectMocks
    private ListProfessionalsByCategoryUseCase useCase;

    private Professional professional(long id, String name, String category) {
        ProfessionalRegistration reg = new ProfessionalRegistration();
        reg.setCategory(category);
        reg.setState("SP");
        reg.setType("CRM");
        reg.setRegistrationNumber("12345");

        Professional p = new Professional();
        p.setId(id);
        p.setName(name);
        p.setWorkSchedule(40);
        p.setRegistration(reg);
        return p;
    }

    @Test
    void execute_returnsEmpty_whenNoProfessionalsInCategory() {
        when(professionalService.listByCategory("TÉCNICO")).thenReturn(List.of());

        assertTrue(useCase.execute(new ProfessionalFilterRequestDTO("TÉCNICO")).isEmpty());
    }

    @Test
    void execute_forwardsFilterCategoryToService() {
        when(professionalService.listByCategory("ENFERMEIRO")).thenReturn(List.of());

        useCase.execute(new ProfessionalFilterRequestDTO("ENFERMEIRO"));

        verify(professionalService).listByCategory("ENFERMEIRO");
    }

    @Test
    void execute_returnsDtoWithCorrectFields() {
        when(professionalService.listByCategory("MÉDICO"))
                .thenReturn(List.of(professional(1L, "Dr. João Silva", "MÉDICO")));

        List<ProfessionalResponseDTO> result = useCase.execute(new ProfessionalFilterRequestDTO("MÉDICO"));

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals("Dr. João Silva", result.get(0).name());
        assertEquals("MÉDICO", result.get(0).registration().category());
    }

    @Test
    void execute_returnsOneDtoPerMatch() {
        when(professionalService.listByCategory("TÉCNICO")).thenReturn(List.of(
                professional(1L, "Téc. Ana Costa",  "TÉCNICO"),
                professional(2L, "Téc. Bruno Reis", "TÉCNICO")
        ));

        assertEquals(2, useCase.execute(new ProfessionalFilterRequestDTO("TÉCNICO")).size());
    }
}
