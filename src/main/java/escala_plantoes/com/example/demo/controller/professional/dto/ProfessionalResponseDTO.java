package escala_plantoes.com.example.demo.controller.professional.dto;

import escala_plantoes.com.example.demo.domain.professional.Professional;

public record ProfessionalResponseDTO(
        Long id,
        String name,
        int workSchedule,
        ProfessionalRegistrationResponseDTO registration
) {
    public static ProfessionalResponseDTO from(Professional professional) {
        return new ProfessionalResponseDTO(
                professional.getId(),
                professional.getName(),
                professional.getWorkSchedule(),
                ProfessionalRegistrationResponseDTO.from(professional.getRegistration())
        );
    }
}
