package escala_plantoes.com.example.demo.controller.dto;

public record ProfessionalRequestDTO(
        String name,
        String registrationNumber,
        String category,
        int workSchedule
) {}
