package escala_plantoes.com.example.demo.controller;

import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void register_returnsNotFound_whenProfessionalDoesNotExist() throws Exception {
        mockMvc.perform(post("/api/plantoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "professionalId", 99999,
                                "data", LocalDate.now().plusDays(1).toString(),
                                "turno", "MANHA"
                        ))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Professional not found: 99999"));
    }

    @Test
    void register_returnsConflict_whenDuplicatePlantao() throws Exception {
        Long professionalId = registerProfessional();
        String date = LocalDate.now().plusDays(1).toString();

        registerPlantao(professionalId, date, "MANHA");

        mockMvc.perform(post("/api/plantoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "professionalId", professionalId,
                                "data", date,
                                "turno", "MANHA"
                        ))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Plantao already exists for this professional, turno and data"));
    }

    private Long registerProfessional() throws Exception {
        String response = mockMvc.perform(post("/api/professionals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "name", "Dr. João Silva",
                                "workSchedule", 40,
                                "registration", Map.of(
                                        "category", "MÉDICO",
                                        "state", "SP",
                                        "type", "CRM",
                                        "registrationNumber", "123456"
                                )
                        ))))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(response, Long.class);
    }

    private Long registerPlantao(Long professionalId, String date, String turno) throws Exception {
        String response = mockMvc.perform(post("/api/plantoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "professionalId", professionalId,
                                "data", date,
                                "turno", turno
                        ))))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).path("id").asLong();
    }
}
