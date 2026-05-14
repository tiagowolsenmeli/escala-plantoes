package escala_plantoes.com.example.demo.profissional.controller.dto;

import escala_plantoes.com.example.demo.profissional.domain.Categoria;
import escala_plantoes.com.example.demo.profissional.domain.CargaHoraria;

public record ProfissionalRequestDTO(
        String nome,
        String cadastroProfissional,
        Categoria categoria,
        CargaHoraria cargaHoraria
) {}
