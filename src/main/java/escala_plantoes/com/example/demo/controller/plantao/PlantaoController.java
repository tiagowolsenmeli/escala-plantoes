package escala_plantoes.com.example.demo.controller.plantao;

import escala_plantoes.com.example.demo.controller.plantao.dto.PlantaoRequestDTO;
import escala_plantoes.com.example.demo.controller.plantao.dto.PlantaoResponseDTO;
import escala_plantoes.com.example.demo.usecase.plantao.DeletePlantaoUseCase;
import escala_plantoes.com.example.demo.usecase.plantao.RegisterPlantaoUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plantoes")
public class PlantaoController {

    private final RegisterPlantaoUseCase registerPlantaoUseCase;
    private final DeletePlantaoUseCase deletePlantaoUseCase;

    public PlantaoController(RegisterPlantaoUseCase registerPlantaoUseCase,
                             DeletePlantaoUseCase deletePlantaoUseCase) {
        this.registerPlantaoUseCase = registerPlantaoUseCase;
        this.deletePlantaoUseCase = deletePlantaoUseCase;
    }

    @PostMapping
    public ResponseEntity<PlantaoResponseDTO> register(@Valid @RequestBody PlantaoRequestDTO dto) {
        return ResponseEntity.ok(registerPlantaoUseCase.execute(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deletePlantaoUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
