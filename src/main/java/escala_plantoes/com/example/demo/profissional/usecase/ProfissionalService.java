package escala_plantoes.com.example.demo.profissional.usecase;

import escala_plantoes.com.example.demo.profissional.domain.Profissional;
import escala_plantoes.com.example.demo.profissional.infrastructure.ProfissionalRepository;
import escala_plantoes.com.example.demo.profissional.controller.dto.ProfissionalRequestDTO;
import escala_plantoes.com.example.demo.profissional.controller.dto.ProfissionalResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfissionalService {

    private final ProfissionalRepository repository;

    public ProfissionalService(ProfissionalRepository repository) {
        this.repository = repository;
    }

    public ProfissionalResponseDTO cadastrar(ProfissionalRequestDTO dto) {
        var profissional = new Profissional();
        profissional.setNome(dto.nome());
        profissional.setCadastroProfissional(dto.cadastroProfissional());
        profissional.setCategoria(dto.categoria());
        profissional.setCargaHoraria(dto.cargaHoraria());

        return ProfissionalResponseDTO.from(repository.save(profissional));
    }

    public List<ProfissionalResponseDTO> listar() {
        return repository.findAll().stream()
                .map(ProfissionalResponseDTO::from)
                .toList();
    }
}
