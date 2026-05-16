package escala_plantoes.com.example.demo.usecase.plantao;

import escala_plantoes.com.example.demo.controller.plantao.dto.PlantaoResponseDTO;
import escala_plantoes.com.example.demo.service.plantao.PlantaoService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ListEscalaUseCase {

    private final PlantaoService plantaoService;

    public ListEscalaUseCase(PlantaoService plantaoService) {
        this.plantaoService = plantaoService;
    }

    public List<PlantaoResponseDTO> execute(LocalDate dataInicio) {
        LocalDate dataFim = dataInicio.plusDays(6);
        return plantaoService.listByPeriod(dataInicio, dataFim)
                .stream()
                .map(PlantaoResponseDTO::from)
                .toList();
    }
}
