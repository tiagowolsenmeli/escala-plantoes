package escala_plantoes.com.example.demo.controller.plantao;

import tools.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
class PlantaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @PersistenceContext
    private EntityManager entityManager;

    // --- register ---

    @Test
    void register_returnsPlantaoOnSuccess() throws Exception {
        Long professionalId = registerProfessional();
        String date = LocalDate.now().plusDays(1).toString();

        mockMvc.perform(post("/plantoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "professionalId", professionalId,
                                "data", date,
                                "turno", "MANHA"
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.professionalId").value(professionalId))
                .andExpect(jsonPath("$.professionalName").value("Dr. João Silva"))
                .andExpect(jsonPath("$.professionalCategory").value("MÉDICO"))
                .andExpect(jsonPath("$.professionalRegistrationNumber").value("123456"))
                .andExpect(jsonPath("$.data").value(date))
                .andExpect(jsonPath("$.turno").value("MANHA"));
    }

    @Test
    void register_allowsSameProfessionalDifferentTurnos() throws Exception {
        Long professionalId = registerProfessional();
        String date = LocalDate.now().plusDays(1).toString();

        registerPlantao(professionalId, date, "MANHA");
        registerPlantao(professionalId, date, "TARDE");

        mockMvc.perform(post("/plantoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "professionalId", professionalId,
                                "data", date,
                                "turno", "NOITE"
                        ))))
                .andExpect(status().isOk());
    }

    @Test
    void register_returnsBadRequest_whenProfessionalIdIsNull() throws Exception {
        mockMvc.perform(post("/plantoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "data", LocalDate.now().plusDays(1).toString(),
                                "turno", "MANHA"
                        ))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_returnsBadRequest_whenProfessionalIdIsZero() throws Exception {
        mockMvc.perform(post("/plantoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "professionalId", 0,
                                "data", LocalDate.now().plusDays(1).toString(),
                                "turno", "MANHA"
                        ))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_returnsBadRequest_whenDataIsNull() throws Exception {
        Long professionalId = registerProfessional();

        mockMvc.perform(post("/plantoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "professionalId", professionalId,
                                "turno", "MANHA"
                        ))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_returnsBadRequest_whenDataIsInThePast() throws Exception {
        Long professionalId = registerProfessional();

        mockMvc.perform(post("/plantoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "professionalId", professionalId,
                                "data", LocalDate.now().minusDays(1).toString(),
                                "turno", "MANHA"
                        ))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_returnsBadRequest_whenTurnoIsNull() throws Exception {
        Long professionalId = registerProfessional();

        mockMvc.perform(post("/plantoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "professionalId", professionalId,
                                "data", LocalDate.now().plusDays(1).toString()
                        ))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_returnsConflict_whenDuplicatePlantao() throws Exception {
        Long professionalId = registerProfessional();
        String date = LocalDate.now().plusDays(1).toString();

        registerPlantao(professionalId, date, "MANHA");

        mockMvc.perform(post("/plantoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "professionalId", professionalId,
                                "data", date,
                                "turno", "MANHA"
                        ))))
                .andExpect(status().isConflict());
    }

    // --- delete ---

    @Test
    void delete_returnsNoContent_whenPlantaoExists() throws Exception {
        Long professionalId = registerProfessional();
        Long plantaoId = registerPlantao(professionalId, LocalDate.now().plusDays(1).toString(), "MANHA");

        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(delete("/plantoes/" + plantaoId))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_returnsNotFound_whenIdNotFound() throws Exception {
        mockMvc.perform(delete("/plantoes/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_returnsNotFound_whenAlreadyDeleted() throws Exception {
        Long professionalId = registerProfessional();
        Long plantaoId = registerPlantao(professionalId, LocalDate.now().plusDays(1).toString(), "TARDE");

        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(delete("/plantoes/" + plantaoId))
                .andExpect(status().isNoContent());

        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(delete("/plantoes/" + plantaoId))
                .andExpect(status().isNotFound());
    }

    // --- helpers ---

    private Long registerProfessional() throws Exception {
        String response = mockMvc.perform(post("/professionals")
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
        String response = mockMvc.perform(post("/plantoes")
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
