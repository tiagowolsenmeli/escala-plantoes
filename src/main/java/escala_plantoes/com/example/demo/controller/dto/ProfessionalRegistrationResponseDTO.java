package escala_plantoes.com.example.demo.controller.dto;

import escala_plantoes.com.example.demo.domain.ProfessionalRegistration;

public record ProfessionalRegistrationResponseDTO(
        Long id,
        String category,
        String state,
        String type,
        String registrationNumber
) {
    public static ProfessionalRegistrationResponseDTO from(ProfessionalRegistration registration) {
        return new ProfessionalRegistrationResponseDTO(
                registration.getId(),
                registration.getCategory(),
                registration.getState(),
                registration.getType(),
                registration.getRegistrationNumber()
        );
    }
}
