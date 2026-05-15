package escala_plantoes.com.example.demo.controller.professional.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ProfessionalRegistrationRequestDTO(
        @NotBlank String category,
        @NotBlank String state,
        @NotBlank String type,
        @NotBlank @Pattern(regexp = "\\d+", message = "Registration number must contain only digits") String registrationNumber
) {}
