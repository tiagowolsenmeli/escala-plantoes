package escala_plantoes.com.example.demo.controller.plantao.dto;

import escala_plantoes.com.example.demo.domain.plantao.Turno;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record PlantaoRequestDTO(
        @NotNull @Positive Long professionalId,
        @NotNull @FutureOrPresent LocalDate data,
        @NotNull Turno turno
) {}
