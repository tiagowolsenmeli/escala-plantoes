package escala_plantoes.com.example.demo.controller.escala;

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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EscalaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void escala_returnsEmpty_whenNoPlantoes() throws Exception {
        mockMvc.perform(get("/api/escala").param("data", LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void escala_returnsBadRequest_whenDataIsMissing() throws Exception {
        mockMvc.perform(get("/api/escala"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void escala_returnsCorrectDtoFields() throws Exception {
        Long professionalId = registerProfessional("Dr. João Silva", "MÉDICO", "123456");
        LocalDate start = LocalDate.now().plusDays(1);

        registerPlantao(professionalId, start.toString(), "MANHA");

        mockMvc.perform(get("/api/escala").param("data", start.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].professionalId").value(professionalId))
                .andExpect(jsonPath("$[0].professionalName").value("Dr. João Silva"))
                .andExpect(jsonPath("$[0].professionalCategory").value("MÉDICO"))
                .andExpect(jsonPath("$[0].professionalRegistrationNumber").value("123456"))
                .andExpect(jsonPath("$[0].plantoes", hasSize(1)))
                .andExpect(jsonPath("$[0].plantoes[0].data").value(start.toString()))
                .andExpect(jsonPath("$[0].plantoes[0].turno").value("MANHA"))
                .andExpect(jsonPath("$[0].plantoes[0].id").isNumber());
    }

    @Test
    void escala_groupsMultiplePlantoesByProfessional() throws Exception {
        Long professionalId = registerProfessional("Dr. João Silva", "MÉDICO", "123456");
        LocalDate start = LocalDate.now().plusDays(1);

        registerPlantao(professionalId, start.toString(), "MANHA");
        registerPlantao(professionalId, start.plusDays(1).toString(), "TARDE");
        registerPlantao(professionalId, start.plusDays(2).toString(), "NOITE");

        mockMvc.perform(get("/api/escala").param("data", start.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].plantoes", hasSize(3)));
    }

    @Test
    void escala_returnsMultipleProfessionals() throws Exception {
        Long id1 = registerProfessional("Dr. João Silva",   "MÉDICO",     "111111");
        Long id2 = registerProfessional("Enf. Maria Souza", "ENFERMEIRO", "222222");
        LocalDate start = LocalDate.now().plusDays(1);

        registerPlantao(id1, start.toString(), "MANHA");
        registerPlantao(id2, start.toString(), "TARDE");

        mockMvc.perform(get("/api/escala").param("data", start.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void escala_includesPlantaoOnStartDate() throws Exception {
        Long professionalId = registerProfessional("Dr. João Silva", "MÉDICO", "123456");
        LocalDate start = LocalDate.now();

        registerPlantao(professionalId, start.toString(), "MANHA");

        mockMvc.perform(get("/api/escala").param("data", start.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].plantoes[0].data").value(start.toString()));
    }

    @Test
    void escala_includesPlantaoOnEndDate() throws Exception {
        Long professionalId = registerProfessional("Dr. João Silva", "MÉDICO", "123456");
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(6);

        registerPlantao(professionalId, end.toString(), "NOITE");

        mockMvc.perform(get("/api/escala").param("data", start.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].plantoes[0].data").value(end.toString()));
    }

    @Test
    void escala_excludesPlantaoBeforeStartDate() throws Exception {
        Long professionalId = registerProfessional("Dr. João Silva", "MÉDICO", "123456");
        // Plantão hoje (válido por @FutureOrPresent), mas escala começa amanhã
        registerPlantao(professionalId, LocalDate.now().toString(), "MANHA");

        mockMvc.perform(get("/api/escala").param("data", LocalDate.now().plusDays(1).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void escala_excludesPlantaoAfterEndDate() throws Exception {
        Long professionalId = registerProfessional("Dr. João Silva", "MÉDICO", "123456");
        LocalDate start = LocalDate.now().plusDays(1);
        // start+7 é o 8º dia, fora do intervalo [start, start+6]
        registerPlantao(professionalId, start.plusDays(7).toString(), "MANHA");

        mockMvc.perform(get("/api/escala").param("data", start.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // --- helpers ---

    private Long registerProfessional(String name, String category, String registrationNumber) throws Exception {
        String response = mockMvc.perform(post("/api/professionals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "name", name,
                                "workSchedule", 40,
                                "registration", Map.of(
                                        "category", category,
                                        "state", "SP",
                                        "type", "CRM",
                                        "registrationNumber", registrationNumber
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
