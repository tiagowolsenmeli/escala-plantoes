package escala_plantoes.com.example.demo.controller.escala.dto;

import escala_plantoes.com.example.demo.domain.plantao.Plantao;

import java.util.List;

public record EscalaResponseDTO(
        Long professionalId,
        String professionalName,
        String professionalCategory,
        String professionalRegistrationNumber,
        List<PlantaoEscalaItemDTO> plantoes
) {
    public static EscalaResponseDTO from(Long professionalId, String name, String category, String registrationNumber, List<Plantao> plantoes) {
        return new EscalaResponseDTO(
                professionalId,
                name,
                category,
                registrationNumber,
                plantoes.stream().map(PlantaoEscalaItemDTO::from).toList()
        );
    }
}
