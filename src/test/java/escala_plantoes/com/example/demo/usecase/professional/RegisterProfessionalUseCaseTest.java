package escala_plantoes.com.example.demo.usecase.professional;

import escala_plantoes.com.example.demo.controller.professional.dto.ProfessionalRegistrationRequestDTO;
import escala_plantoes.com.example.demo.controller.professional.dto.ProfessionalRequestDTO;
import escala_plantoes.com.example.demo.controller.professional.dto.ProfessionalResponseDTO;
import escala_plantoes.com.example.demo.domain.professional.Professional;
import escala_plantoes.com.example.demo.domain.professional.ProfessionalRegistration;
import escala_plantoes.com.example.demo.exception.BadRequestException;
import escala_plantoes.com.example.demo.service.professional.ProfessionalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterProfessionalUseCaseTest {

    @Mock
    private ProfessionalService professionalService;

    @InjectMocks
    private RegisterProfessionalUseCase useCase;

    private static final ProfessionalRegistrationRequestDTO VALID_REGISTRATION =
            new ProfessionalRegistrationRequestDTO("MÉDICO", "SP", "CRM", "12345");

    private Professional savedProfessional() {
        ProfessionalRegistration reg = new ProfessionalRegistration();
        reg.setId(2L);
        reg.setCategory("MÉDICO");
        reg.setState("SP");
        reg.setType("CRM");
        reg.setRegistrationNumber("12345");

        Professional p = new Professional();
        p.setId(1L);
        p.setName("Dr. João Silva");
        p.setWorkSchedule(40);
        p.setRegistration(reg);
        return p;
    }

    @Test
    void execute_returnsDto_whenValid() {
        ProfessionalRequestDTO dto = new ProfessionalRequestDTO("Dr. João Silva", 40, VALID_REGISTRATION);
        when(professionalService.register(any())).thenReturn(savedProfessional());

        ProfessionalResponseDTO result = useCase.execute(dto);

        assertEquals(1L, result.id());
        assertEquals("Dr. João Silva", result.name());
        assertEquals(40, result.workSchedule());
        assertEquals("MÉDICO", result.registration().category());
        assertEquals("SP", result.registration().state());
        assertEquals("CRM", result.registration().type());
        assertEquals("12345", result.registration().registrationNumber());
    }

    @Test
    void execute_savesEntityWithCorrectFields() {
        ProfessionalRequestDTO dto = new ProfessionalRequestDTO("Enf. Maria Souza", 30,
                new ProfessionalRegistrationRequestDTO("ENFERMEIRO", "RJ", "COREN", "99999"));
        when(professionalService.register(any())).thenReturn(savedProfessional());

        useCase.execute(dto);

        ArgumentCaptor<Professional> captor = ArgumentCaptor.forClass(Professional.class);
        verify(professionalService).register(captor.capture());
        Professional saved = captor.getValue();
        assertEquals("Enf. Maria Souza", saved.getName());
        assertEquals(30, saved.getWorkSchedule());
        assertEquals("ENFERMEIRO", saved.getRegistration().getCategory());
        assertEquals("RJ", saved.getRegistration().getState());
        assertEquals("COREN", saved.getRegistration().getType());
        assertEquals("99999", saved.getRegistration().getRegistrationNumber());
    }

    @Test
    void execute_throwsBadRequestException_whenValidationFails() {
        ProfessionalRequestDTO dto = new ProfessionalRequestDTO("Dr. João Silva", 25, VALID_REGISTRATION);

        assertThrows(BadRequestException.class, () -> useCase.execute(dto));
        verify(professionalService, never()).register(any());
    }
}
