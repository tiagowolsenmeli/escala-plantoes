package escala_plantoes.com.example.demo.controller.professional;

import escala_plantoes.com.example.demo.controller.professional.dto.ProfessionalFilterRequestDTO;
import escala_plantoes.com.example.demo.controller.professional.dto.ProfessionalRequestDTO;
import escala_plantoes.com.example.demo.controller.professional.dto.ProfessionalResponseDTO;
import escala_plantoes.com.example.demo.usecase.professional.ListProfessionalsByCategoryUseCase;
import escala_plantoes.com.example.demo.usecase.professional.ListProfessionalsUseCase;
import escala_plantoes.com.example.demo.usecase.professional.RegisterProfessionalUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professionals")
public class ProfessionalController {

    private final RegisterProfessionalUseCase registerUseCase;
    private final ListProfessionalsUseCase listUseCase;
    private final ListProfessionalsByCategoryUseCase listByCategoryUseCase;

    public ProfessionalController(RegisterProfessionalUseCase registerUseCase,
                                  ListProfessionalsUseCase listUseCase,
                                  ListProfessionalsByCategoryUseCase listByCategoryUseCase) {
        this.registerUseCase = registerUseCase;
        this.listUseCase = listUseCase;
        this.listByCategoryUseCase = listByCategoryUseCase;
    }

    @PostMapping
    public ResponseEntity<Long> register(@Valid @RequestBody ProfessionalRequestDTO dto) {
        ProfessionalResponseDTO response = registerUseCase.execute(dto);
        return ResponseEntity.ok(response.id());
    }

    @GetMapping
    public ResponseEntity<List<ProfessionalResponseDTO>> list() {
        return ResponseEntity.ok(listUseCase.execute());
    }

    @GetMapping("/category")
    public ResponseEntity<List<ProfessionalResponseDTO>> listByCategory(
            @Valid @RequestBody ProfessionalFilterRequestDTO filter) {
        return ResponseEntity.ok(listByCategoryUseCase.execute(filter));
    }
}
