package escala_plantoes.com.example.demo.usecase;

import escala_plantoes.com.example.demo.controller.dto.ProfessionalResponseDTO;
import escala_plantoes.com.example.demo.service.ProfessionalService;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ListProfessionalsUseCase {

    private final ProfessionalService service;

    public ListProfessionalsUseCase(ProfessionalService service) {
        this.service = service;
    }

    public List<ProfessionalResponseDTO> execute() {
        return service.list();
    }
}
