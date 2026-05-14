package escala_plantoes.com.example.demo.usecase;

import escala_plantoes.com.example.demo.controller.dto.ProfessionalRequestDTO;
import escala_plantoes.com.example.demo.controller.dto.ProfessionalResponseDTO;
import escala_plantoes.com.example.demo.domain.Professional;
import escala_plantoes.com.example.demo.domain.ProfessionalRegistration;
import escala_plantoes.com.example.demo.service.ProfessionalService;

public class RegisterProfessionalUseCase {

    private final ProfessionalService service;

    public RegisterProfessionalUseCase(ProfessionalService service) {
        this.service = service;
    }

    public ProfessionalResponseDTO execute(ProfessionalRequestDTO dto) {
        ProfessionalRegistration registration = new ProfessionalRegistration();
        registration.setCategory(dto.registration().category());
        registration.setState(dto.registration().state());
        registration.setType(dto.registration().type());
        registration.setRegistrationNumber(dto.registration().registrationNumber());

        Professional professional = new Professional();
        professional.setName(dto.name());
        professional.setWorkSchedule(dto.workSchedule());
        professional.setRegistration(registration);

        return ProfessionalResponseDTO.from(service.register(professional));
    }
}
