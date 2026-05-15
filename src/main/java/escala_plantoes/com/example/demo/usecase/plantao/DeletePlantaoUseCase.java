package escala_plantoes.com.example.demo.usecase.plantao;

import escala_plantoes.com.example.demo.service.plantao.PlantaoService;
import org.springframework.stereotype.Component;

@Component
public class DeletePlantaoUseCase {

    private final PlantaoService plantaoService;

    public DeletePlantaoUseCase(PlantaoService plantaoService) {
        this.plantaoService = plantaoService;
    }

    public void execute(Long id) {
        plantaoService.delete(id);
    }
}
