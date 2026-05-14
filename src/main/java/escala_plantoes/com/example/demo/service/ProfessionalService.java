package escala_plantoes.com.example.demo.service;

import escala_plantoes.com.example.demo.controller.dto.ProfessionalResponseDTO;
import escala_plantoes.com.example.demo.domain.Professional;
import escala_plantoes.com.example.demo.infrastructure.ProfessionalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessionalService {

    private final ProfessionalRepository repository;

    public ProfessionalService(ProfessionalRepository repository) {
        this.repository = repository;
    }

    public Professional register(Professional professional) {
        return repository.save(professional);
    }

    public List<ProfessionalResponseDTO> list() {
        return repository.findAll().stream()
                .map(ProfessionalResponseDTO::from)
                .toList();
    }
}
