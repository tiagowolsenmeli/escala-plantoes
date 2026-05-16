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

    // --- carga horária ---

    @Test
    void register_succeedsWhenTotalHoursIsExactlyAtLimit() throws Exception {
        // 30h schedule: NOITE(12) + NOITE(12) = 24h na janela, + MANHA(6) = 30h = limite
        Long professionalId = registerProfessionalWithSchedule(30);
        registerPlantao(professionalId, LocalDate.now().plusDays(1).toString(), "NOITE"); // 12h
        registerPlantao(professionalId, LocalDate.now().plusDays(2).toString(), "NOITE"); // 12h

        // 24h acumulados + 6h = 30h = limite → deve aceitar
        mockMvc.perform(post("/plantoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "professionalId", professionalId,
                                "data", LocalDate.now().plusDays(3).toString(),
                                "turno", "MANHA"
                        ))))
                .andExpect(status().isOk());
    }

    @Test
    void register_returnsUnprocessableEntity_whenCargaHorariaExceeded() throws Exception {
        // 20h schedule: NOITE(12) já cadastrado, novo NOITE(12) = 24h > 20h
        Long professionalId = registerProfessionalWithSchedule(20);
        registerPlantao(professionalId, LocalDate.now().plusDays(1).toString(), "NOITE"); // 12h

        // 12h + 12h = 24h > 20h → deve rejeitar com 422 e mensagem
        mockMvc.perform(post("/plantoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "professionalId", professionalId,
                                "data", LocalDate.now().plusDays(2).toString(),
                                "turno", "NOITE"
                        ))))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void register_countsNightShiftAs12Hours() throws Exception {
        // 20h schedule: NOITE vale 12h, então 1 NOITE + 1 MANHA já = 18h; mais 1 TARDE = 24h > 20h
        Long professionalId = registerProfessionalWithSchedule(20);
        registerPlantao(professionalId, LocalDate.now().plusDays(1).toString(), "NOITE"); // 12h
        registerPlantao(professionalId, LocalDate.now().plusDays(2).toString(), "MANHA"); // 6h

        // 18h acumulados + 6h = 24h > 20h → deve rejeitar (NOITE contado como 12h)
        mockMvc.perform(post("/plantoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "professionalId", professionalId,
                                "data", LocalDate.now().plusDays(3).toString(),
                                "turno", "TARDE"
                        ))))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void register_doesNotCountPlantoesOutsideWindow() throws Exception {
        // 20h schedule: NOITE(12h) em D+1 — 4 dias antes de D+5, fora da janela [D+2, D+8]
        Long professionalId = registerProfessionalWithSchedule(20);
        registerPlantao(professionalId, LocalDate.now().plusDays(1).toString(), "NOITE");

        // 0h na janela de D+5 + 12h novo = 12h ≤ 20h → deve aceitar
        mockMvc.perform(post("/plantoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "professionalId", professionalId,
                                "data", LocalDate.now().plusDays(5).toString(),
                                "turno", "NOITE"
                        ))))
                .andExpect(status().isOk());
    }

    @Test
    void register_countsPlantaoExactly3DaysBeforeTarget() throws Exception {
        // 20h schedule: NOITE(12h) em D+1 — exatamente 3 dias antes de D+4, dentro da janela [D+1, D+7]
        Long professionalId = registerProfessionalWithSchedule(20);
        registerPlantao(professionalId, LocalDate.now().plusDays(1).toString(), "NOITE");

        // 12h + 12h = 24h > 20h → D+1 deve ser contado na janela de D+4
        mockMvc.perform(post("/plantoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "professionalId", professionalId,
                                "data", LocalDate.now().plusDays(4).toString(),
                                "turno", "NOITE"
                        ))))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void register_doesNotCountPlantaoExactly4DaysBeforeTarget() throws Exception {
        // 20h schedule: NOITE(12h) em D+1 — 4 dias antes de D+5, fora da janela [D+2, D+8]
        Long professionalId = registerProfessionalWithSchedule(20);
        registerPlantao(professionalId, LocalDate.now().plusDays(1).toString(), "NOITE");

        // 0h na janela de D+5 + 12h novo = 12h ≤ 20h → D+1 não deve ser contado
        mockMvc.perform(post("/plantoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "professionalId", professionalId,
                                "data", LocalDate.now().plusDays(5).toString(),
                                "turno", "NOITE"
                        ))))
                .andExpect(status().isOk());
    }

    @Test
    void register_countsPlantaoExactly3DaysAfterTarget() throws Exception {
        // 20h schedule: NOITE(12h) em D+4 — exatamente 3 dias depois de D+1, dentro da janela [D-2, D+4]
        Long professionalId = registerProfessionalWithSchedule(20);
        registerPlantao(professionalId, LocalDate.now().plusDays(4).toString(), "NOITE");

        // 12h + 12h = 24h > 20h → D+4 deve ser contado na janela de D+1
        mockMvc.perform(post("/plantoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "professionalId", professionalId,
                                "data", LocalDate.now().plusDays(1).toString(),
                                "turno", "NOITE"
                        ))))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void register_doesNotCountPlantaoExactly4DaysAfterTarget() throws Exception {
        // 20h schedule: NOITE(12h) em D+5 — 4 dias depois de D+1, fora da janela [D-2, D+4]
        Long professionalId = registerProfessionalWithSchedule(20);
        registerPlantao(professionalId, LocalDate.now().plusDays(5).toString(), "NOITE");

        // 0h na janela de D+1 + 12h novo = 12h ≤ 20h → D+5 não deve ser contado
        mockMvc.perform(post("/plantoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "professionalId", professionalId,
                                "data", LocalDate.now().plusDays(1).toString(),
                                "turno", "NOITE"
                        ))))
                .andExpect(status().isOk());
    }

    @Test
    void register_accumulatesHoursAcrossMultipleDaysInWindow() throws Exception {
        // 30h schedule: 3 plantões espalhados na janela somam 30h; novo excede
        Long professionalId = registerProfessionalWithSchedule(30);
        LocalDate base = LocalDate.now().plusDays(4);

        registerPlantao(professionalId, base.minusDays(2).toString(), "NOITE"); // 12h
        registerPlantao(professionalId, base.minusDays(1).toString(), "NOITE"); // 12h
        registerPlantao(professionalId, base.plusDays(1).toString(), "MANHA"); // 6h

        // 30h acumulados na janela de base + 6h novo = 36h > 30h → deve rejeitar
        mockMvc.perform(post("/plantoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "professionalId", professionalId,
                                "data", base.toString(),
                                "turno", "TARDE"
                        ))))
                .andExpect(status().isUnprocessableEntity());
    }

    // --- helpers ---

    private Long registerProfessional() throws Exception {
        return registerProfessionalWithSchedule(40);
    }

    private Long registerProfessionalWithSchedule(int workSchedule) throws Exception {
        String response = mockMvc.perform(post("/professionals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "name", "Dr. João Silva",
                                "workSchedule", workSchedule,
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
