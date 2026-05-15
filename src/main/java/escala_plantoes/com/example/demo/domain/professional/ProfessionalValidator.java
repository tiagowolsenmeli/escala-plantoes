package escala_plantoes.com.example.demo.domain.professional;

import escala_plantoes.com.example.demo.controller.professional.dto.ProfessionalRequestDTO;
import escala_plantoes.com.example.demo.exception.BadRequestException;

import java.util.List;

public class ProfessionalValidator {

    private static final List<Integer> VALID_WORK_SCHEDULES = List.of(20, 30, 40);
    private static final List<String> VALID_CATEGORIES = List.of("MÉDICO", "ENFERMEIRO", "TÉCNICO");

    public static void validate(ProfessionalRequestDTO dto) {
        if (dto.name() == null) {
            throw new BadRequestException("Name cannot be null");
        }
        if (dto.registration() == null) {
            throw new BadRequestException("Registration cannot be null");
        }
        if (dto.registration().registrationNumber() == null) {
            throw new BadRequestException("Registration number cannot be null");
        }
        if (!dto.registration().registrationNumber().matches("\\d+")) {
            throw new BadRequestException("Registration number must contain only digits");
        }
        if (!VALID_WORK_SCHEDULES.contains(dto.workSchedule())) {
            throw new BadRequestException("Work schedule must be 20, 30 or 40 hours");
        }
        if (!VALID_CATEGORIES.contains(dto.registration().category())) {
            throw new BadRequestException("Category must be MÉDICO, ENFERMEIRO or TÉCNICO");
        }
    }
}
