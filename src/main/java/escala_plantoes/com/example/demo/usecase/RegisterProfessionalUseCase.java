package escala_plantoes.com.example.demo.usecase;

import escala_plantoes.com.example.demo.controller.dto.ProfessionalRequestDTO;
import escala_plantoes.com.example.demo.controller.dto.ProfessionalResponseDTO;
import escala_plantoes.com.example.demo.domain.Professional;
import escala_plantoes.com.example.demo.service.ProfessionalService;
import org.springframework.stereotype.Component;

@Component
public class RegisterProfessionalUseCase {

    private final ProfessionalService service;

    public RegisterProfessionalUseCase(ProfessionalService service) {
        this.service = service;
    }

    public ProfessionalResponseDTO execute(ProfessionalRequestDTO dto) {

        Professional professional = new Professional();
        professional.setName(dto.name());
        professional.setRegistrationNumber(dto.registrationNumber());
        professional.setCategory(dto.category());
        professional.setWorkSchedule(dto.workSchedule());

        return ProfessionalResponseDTO.from(service.register(professional));
    }
}
