package escala_plantoes.com.example.demo.usecase.professional;

import escala_plantoes.com.example.demo.controller.professional.dto.ProfessionalFilterRequestDTO;
import escala_plantoes.com.example.demo.controller.professional.dto.ProfessionalResponseDTO;
import escala_plantoes.com.example.demo.service.professional.ProfessionalService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListProfessionalsByCategoryUseCase {

    private final ProfessionalService service;

    public ListProfessionalsByCategoryUseCase(ProfessionalService service) {
        this.service = service;
    }

    public List<ProfessionalResponseDTO> execute(ProfessionalFilterRequestDTO filter) {
        return service.listByCategory(filter.category())
                .stream()
                .map(ProfessionalResponseDTO::from)
                .toList();
    }
}
