package escala_plantoes.com.example.demo.usecase.plantao;

import escala_plantoes.com.example.demo.controller.plantao.dto.PlantaoRequestDTO;
import escala_plantoes.com.example.demo.controller.plantao.dto.PlantaoResponseDTO;
import escala_plantoes.com.example.demo.domain.plantao.Plantao;
import escala_plantoes.com.example.demo.domain.professional.Professional;
import escala_plantoes.com.example.demo.service.plantao.PlantaoService;
import escala_plantoes.com.example.demo.service.professional.ProfessionalService;
import org.springframework.stereotype.Component;

@Component
public class RegisterPlantaoUseCase {

    private final PlantaoService plantaoService;
    private final ProfessionalService professionalService;

    public RegisterPlantaoUseCase(PlantaoService plantaoService, ProfessionalService professionalService) {
        this.plantaoService = plantaoService;
        this.professionalService = professionalService;
    }

    public PlantaoResponseDTO execute(PlantaoRequestDTO dto) {
        Professional professional = professionalService.findById(dto.professionalId());

        if (plantaoService.existsByProfessionalAndTurnoAndData(dto.professionalId(), dto.turno(), dto.data())) {
            throw new IllegalStateException("Plantao already exists for this professional, turno and data");
        }

        Plantao plantao = new Plantao();
        plantao.setProfessional(professional);
        plantao.setData(dto.data());
        plantao.setTurno(dto.turno());

        return PlantaoResponseDTO.from(plantaoService.register(plantao));
    }
}
