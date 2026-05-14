package escala_plantoes.com.example.demo.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProfessionalRequestDTO(
        @NotBlank String name,
        @Positive int workSchedule,
        @NotNull @Valid ProfessionalRegistrationRequestDTO registration
) {}
