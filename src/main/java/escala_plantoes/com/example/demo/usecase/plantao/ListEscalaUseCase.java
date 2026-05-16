package escala_plantoes.com.example.demo.usecase.plantao;

import escala_plantoes.com.example.demo.controller.escala.dto.EscalaResponseDTO;
import escala_plantoes.com.example.demo.domain.plantao.Plantao;
import escala_plantoes.com.example.demo.domain.professional.Professional;
import escala_plantoes.com.example.demo.service.plantao.PlantaoService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ListEscalaUseCase {

    private final PlantaoService plantaoService;

    public ListEscalaUseCase(PlantaoService plantaoService) {
        this.plantaoService = plantaoService;
    }

    public List<EscalaResponseDTO> execute(LocalDate dataInicio) {
        LocalDate dataFim = dataInicio.plusDays(6);
        Map<Professional, List<Plantao>> byProfessional = plantaoService.listByPeriod(dataInicio, dataFim)
                .stream()
                .collect(Collectors.groupingBy(Plantao::getProfessional));

        return byProfessional.entrySet().stream()
                .map(e -> {
                    Professional p = e.getKey();
                    return EscalaResponseDTO.from(
                            p.getId(),
                            p.getName(),
                            p.getRegistration().getCategory(),
                            p.getRegistration().getRegistrationNumber(),
                            e.getValue()
                    );
                })
                .toList();
    }
}
