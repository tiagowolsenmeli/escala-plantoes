package escala_plantoes.com.example.demo.controller.dto;

import escala_plantoes.com.example.demo.domain.Professional;

public record ProfessionalResponseDTO(
        Long id,
        String name,
        String registrationNumber,
        String category,
        int workSchedule
) {
    public static ProfessionalResponseDTO from(Professional professional) {
        return new ProfessionalResponseDTO(
                professional.getId(),
                professional.getName(),
                professional.getRegistrationNumber(),
                professional.getCategory(),
                professional.getWorkSchedule()
        );
    }
}
