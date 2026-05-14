package escala_plantoes.com.example.demo.profissional.controller.dto;

import escala_plantoes.com.example.demo.profissional.domain.Categoria;
import escala_plantoes.com.example.demo.profissional.domain.Profissional;

public record ProfissionalResponseDTO(
        Long id,
        String nome,
        String cadastroProfissional,
        Categoria categoria,
        int cargaHoraria
) {
    public static ProfissionalResponseDTO from(Profissional profissional) {
        return new ProfissionalResponseDTO(
                profissional.getId(),
                profissional.getNome(),
                profissional.getCadastroProfissional(),
                profissional.getCategoria(),
                profissional.getCargaHoraria().getHoras()
        );
    }
}
