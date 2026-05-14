package escala_plantoes.com.example.demo.controller;

import escala_plantoes.com.example.demo.controller.dto.ProfessionalRequestDTO;
import escala_plantoes.com.example.demo.controller.dto.ProfessionalResponseDTO;
import escala_plantoes.com.example.demo.usecase.ListProfessionalsUseCase;
import escala_plantoes.com.example.demo.usecase.RegisterProfessionalUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professionals")
public class ProfessionalController {

    private final RegisterProfessionalUseCase registerUseCase;
    private final ListProfessionalsUseCase listUseCase;

    public ProfessionalController(RegisterProfessionalUseCase registerUseCase,
                                  ListProfessionalsUseCase listUseCase) {
        this.registerUseCase = registerUseCase;
        this.listUseCase = listUseCase;
    }

    @PostMapping
    public ResponseEntity<Long> register(@RequestBody ProfessionalRequestDTO dto) {
        ProfessionalResponseDTO response = registerUseCase.execute(dto);
        return ResponseEntity.ok(response.id());
    }

    @GetMapping
    public ResponseEntity<List<ProfessionalResponseDTO>> list() {
        return ResponseEntity.ok(listUseCase.execute());
    }
}
