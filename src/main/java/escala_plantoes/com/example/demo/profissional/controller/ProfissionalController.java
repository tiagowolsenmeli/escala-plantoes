package escala_plantoes.com.example.demo.profissional.controller;

import escala_plantoes.com.example.demo.profissional.controller.dto.ProfissionalRequestDTO;
import escala_plantoes.com.example.demo.profissional.controller.dto.ProfissionalResponseDTO;
import escala_plantoes.com.example.demo.profissional.usecase.ProfissionalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/profissionais")
public class ProfissionalController {

    private final ProfissionalService service;

    public ProfissionalController(ProfissionalService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProfissionalResponseDTO> cadastrar(
            @RequestBody ProfissionalRequestDTO dto,
            UriComponentsBuilder uriBuilder) {

        ProfissionalResponseDTO response = service.cadastrar(dto);
        var uri = uriBuilder.path("/api/profissionais/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProfissionalResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }
}
