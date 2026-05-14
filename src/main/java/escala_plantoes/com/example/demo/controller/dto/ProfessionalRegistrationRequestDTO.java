package escala_plantoes.com.example.demo.controller.dto;

public record ProfessionalRegistrationRequestDTO(
        String category,
        String state,
        String type,
        String registrationNumber
) {}
