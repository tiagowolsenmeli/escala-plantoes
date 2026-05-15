package escala_plantoes.com.example.demo.controller.professional;

import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProfessionalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String validRequest() throws Exception {
        return new ClassPathResource("fixtures/professional/valid-professional-request.json")
                .getContentAsString(StandardCharsets.UTF_8);
    }

    @Test
    void register_returnsIdOnSuccess() throws Exception {
        mockMvc.perform(post("/professionals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNumber());
    }

    @Test
    void register_persistsProfessionalVisibleInList() throws Exception {
        mockMvc.perform(post("/professionals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/professionals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].name").value("Dr. João Silva"))
                .andExpect(jsonPath("$[0].workSchedule").value(40))
                .andExpect(jsonPath("$[0].registration.category").value("MÉDICO"))
                .andExpect(jsonPath("$[0].registration.state").value("SP"))
                .andExpect(jsonPath("$[0].registration.registrationNumber").value("123456"));
    }

    @Test
    void register_returnsBadRequest_whenNameIsBlank() throws Exception {
        Map<String, Object> request = Map.of(
                "name", "",
                "workSchedule", 40,
                "registration", Map.of(
                        "category", "MÉDICO",
                        "state", "SP",
                        "type", "CRM",
                        "registrationNumber", "123456"
                )
        );

        mockMvc.perform(post("/professionals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_returnsBadRequest_whenRegistrationNumberContainsLetters() throws Exception {
        Map<String, Object> request = Map.of(
                "name", "Dra. Maria",
                "workSchedule", 30,
                "registration", Map.of(
                        "category", "ENFERMEIRO",
                        "state", "RJ",
                        "type", "COREN",
                        "registrationNumber", "ABC123"
                )
        );

        mockMvc.perform(post("/professionals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_returnsBadRequest_whenRegistrationIsMissing() throws Exception {
        Map<String, Object> request = Map.of(
                "name", "Dr. Carlos",
                "workSchedule", 20
        );

        mockMvc.perform(post("/professionals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_returnsBadRequest_whenWorkScheduleIsZero() throws Exception {
        Map<String, Object> request = Map.of(
                "name", "Dr. Pedro",
                "workSchedule", 0,
                "registration", Map.of(
                        "category", "MÉDICO",
                        "state", "MG",
                        "type", "CRM",
                        "registrationNumber", "654321"
                )
        );

        mockMvc.perform(post("/professionals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // --- listByCategory ---

    @Test
    void listByCategory_returnsCorrectCountPerCategory() throws Exception {
        register("Dr. João Silva",    40, "MÉDICO",     "SP", "CRM",   "100001");
        register("Enf. Maria Souza",  30, "ENFERMEIRO", "RJ", "COREN", "200001");
        register("Enf. Carlos Lima",  20, "ENFERMEIRO", "MG", "COREN", "200002");
        register("Téc. Ana Costa",    20, "TÉCNICO",    "SP", "COREN", "300001");
        register("Téc. Bruno Reis",   20, "TÉCNICO",    "RJ", "COREN", "300002");
        register("Téc. Clara Nunes",  20, "TÉCNICO",    "MG", "COREN", "300003");
        register("Téc. Diego Pinto",  30, "TÉCNICO",    "BA", "COREN", "300004");
        register("Téc. Eva Martins",  40, "TÉCNICO",    "PR", "COREN", "300005");

        listByCategory("MÉDICO")
                .andExpect(jsonPath("$", hasSize(1)));

        listByCategory("ENFERMEIRO")
                .andExpect(jsonPath("$", hasSize(2)));

        listByCategory("TÉCNICO")
                .andExpect(jsonPath("$", hasSize(5)));
    }

    @Test
    void listByCategory_medico_returnsCorrectValues() throws Exception {
        register("Dr. João Silva", 40, "MÉDICO", "SP", "CRM", "100001");

        listByCategory("MÉDICO")
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Dr. João Silva"))
                .andExpect(jsonPath("$[0].workSchedule").value(40))
                .andExpect(jsonPath("$[0].registration.category").value("MÉDICO"))
                .andExpect(jsonPath("$[0].registration.state").value("SP"))
                .andExpect(jsonPath("$[0].registration.type").value("CRM"))
                .andExpect(jsonPath("$[0].registration.registrationNumber").value("100001"));
    }

    @Test
    void listByCategory_enfermeiro_returnsCorrectValues() throws Exception {
        register("Enf. Maria Souza", 30, "ENFERMEIRO", "RJ", "COREN", "200001");
        register("Enf. Carlos Lima", 20, "ENFERMEIRO", "MG", "COREN", "200002");

        listByCategory("ENFERMEIRO")
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].registration.category", everyItem(equalTo("ENFERMEIRO"))))
                .andExpect(jsonPath("$[*].name",
                        containsInAnyOrder("Enf. Maria Souza", "Enf. Carlos Lima")))
                .andExpect(jsonPath("$[*].registration.registrationNumber",
                        containsInAnyOrder("200001", "200002")));
    }

    @Test
    void listByCategory_tecnico_returnsCorrectValues() throws Exception {
        register("Téc. Ana Costa",   20, "TÉCNICO", "SP", "COREN", "300001");
        register("Téc. Bruno Reis",  20, "TÉCNICO", "RJ", "COREN", "300002");
        register("Téc. Clara Nunes", 20, "TÉCNICO", "MG", "COREN", "300003");
        register("Téc. Diego Pinto", 30, "TÉCNICO", "BA", "COREN", "300004");
        register("Téc. Eva Martins", 40, "TÉCNICO", "PR", "COREN", "300005");

        listByCategory("TÉCNICO")
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[*].registration.category", everyItem(equalTo("TÉCNICO"))))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(
                        "Téc. Ana Costa", "Téc. Bruno Reis", "Téc. Clara Nunes",
                        "Téc. Diego Pinto", "Téc. Eva Martins")))
                .andExpect(jsonPath("$[*].registration.registrationNumber", containsInAnyOrder(
                        "300001", "300002", "300003", "300004", "300005")))
                .andExpect(jsonPath("$[*].workSchedule", containsInAnyOrder(20, 20, 20, 30, 40)));
    }

    @Test
    void listByCategory_returnsEmpty_whenNoProfessionalsInCategory() throws Exception {
        register("Dr. João Silva", 40, "MÉDICO", "SP", "CRM", "100001");

        listByCategory("ENFERMEIRO")
                .andExpect(jsonPath("$", hasSize(0)));
    }

    private void register(String name, int workSchedule, String category,
                          String state, String type, String registrationNumber) throws Exception {
        mockMvc.perform(post("/professionals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "name", name,
                                "workSchedule", workSchedule,
                                "registration", Map.of(
                                        "category", category,
                                        "state", state,
                                        "type", type,
                                        "registrationNumber", registrationNumber
                                )
                        ))))
                .andExpect(status().isOk());
    }

    private org.springframework.test.web.servlet.ResultActions listByCategory(String category) throws Exception {
        return mockMvc.perform(get("/professionals/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("category", category))))
                .andExpect(status().isOk());
    }
}
