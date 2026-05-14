package escala_plantoes.com.example.demo.controller.dto;

import escala_plantoes.com.example.demo.domain.Professional;

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
