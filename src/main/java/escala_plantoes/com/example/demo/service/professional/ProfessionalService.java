package escala_plantoes.com.example.demo.service.professional;

import escala_plantoes.com.example.demo.domain.professional.Professional;
import escala_plantoes.com.example.demo.infrastructure.professional.ProfessionalRepository;
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

    public List<Professional> list() {
        return repository.findAll();
    }

    public List<Professional> listByCategory(String category) {
        return repository.findAllByRegistration_Category(category);
    }

    public Professional findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Professional not found: " + id));
    }
}
