package escala_plantoes.com.example.demo.controller.professional.dto;

import jakarta.validation.constraints.NotBlank;

public record ProfessionalFilterRequestDTO(
        @NotBlank String category
) {
}
