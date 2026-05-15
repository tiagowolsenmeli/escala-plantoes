package escala_plantoes.com.example.demo.usecase.professional;

import escala_plantoes.com.example.demo.controller.professional.dto.ProfessionalRequestDTO;
import escala_plantoes.com.example.demo.controller.professional.dto.ProfessionalResponseDTO;
import escala_plantoes.com.example.demo.domain.professional.Professional;
import escala_plantoes.com.example.demo.domain.professional.ProfessionalRegistration;
import escala_plantoes.com.example.demo.domain.professional.ProfessionalValidator;
import escala_plantoes.com.example.demo.service.professional.ProfessionalService;
import org.springframework.stereotype.Component;

@Component
public class RegisterProfessionalUseCase {

    private final ProfessionalService service;

    public RegisterProfessionalUseCase(ProfessionalService service) {
        this.service = service;
    }

    public ProfessionalResponseDTO execute(ProfessionalRequestDTO dto) {
        ProfessionalValidator.validate(dto);
        Professional professional = getProfessional(dto);
        return ProfessionalResponseDTO.from(service.register(professional));
    }

    private static Professional getProfessional(ProfessionalRequestDTO dto) {
        ProfessionalRegistration registration = new ProfessionalRegistration();
        registration.setCategory(dto.registration().category());
        registration.setState(dto.registration().state());
        registration.setType(dto.registration().type());
        registration.setRegistrationNumber(dto.registration().registrationNumber());

        Professional professional = new Professional();
        professional.setName(dto.name());
        professional.setWorkSchedule(dto.workSchedule());
        professional.setRegistration(registration);
        return professional;
    }
}
