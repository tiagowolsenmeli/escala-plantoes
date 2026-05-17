package escala_plantoes.com.example.demo.controller.escala;

import escala_plantoes.com.example.demo.controller.escala.dto.EscalaResponseDTO;
import escala_plantoes.com.example.demo.usecase.plantao.ListEscalaUseCase;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/escala")
public class EscalaController {

    private final ListEscalaUseCase listEscalaUseCase;

    public EscalaController(ListEscalaUseCase listEscalaUseCase) {
        this.listEscalaUseCase = listEscalaUseCase;
    }

    @GetMapping
    public ResponseEntity<List<EscalaResponseDTO>> escala(
            @NotNull @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(listEscalaUseCase.execute(data));
    }
}
