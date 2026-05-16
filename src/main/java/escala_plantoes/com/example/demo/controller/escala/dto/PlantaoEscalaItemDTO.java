package escala_plantoes.com.example.demo.controller.escala.dto;

import escala_plantoes.com.example.demo.domain.plantao.Plantao;
import escala_plantoes.com.example.demo.domain.plantao.Turno;

import java.time.LocalDate;

public record PlantaoEscalaItemDTO(
        Long id,
        LocalDate data,
        Turno turno
) {
    public static PlantaoEscalaItemDTO from(Plantao plantao) {
        return new PlantaoEscalaItemDTO(
                plantao.getId(),
                plantao.getData(),
                plantao.getTurno()
        );
    }
}
