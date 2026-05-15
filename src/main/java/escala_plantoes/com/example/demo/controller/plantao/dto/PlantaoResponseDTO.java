package escala_plantoes.com.example.demo.controller.plantao.dto;

import escala_plantoes.com.example.demo.domain.plantao.Plantao;
import escala_plantoes.com.example.demo.domain.plantao.Turno;

import java.time.LocalDate;

public record PlantaoResponseDTO(
        Long id,
        Long professionalId,
        String professionalName,
        String professionalCategory,
        String professionalRegistrationNumber,
        LocalDate data,
        Turno turno
) {
    public static PlantaoResponseDTO from(Plantao plantao) {
        return new PlantaoResponseDTO(
                plantao.getId(),
                plantao.getProfessional().getId(),
                plantao.getProfessional().getName(),
                plantao.getProfessional().getRegistration().getCategory(),
                plantao.getProfessional().getRegistration().getRegistrationNumber(),
                plantao.getData(),
                plantao.getTurno()
        );
    }
}
