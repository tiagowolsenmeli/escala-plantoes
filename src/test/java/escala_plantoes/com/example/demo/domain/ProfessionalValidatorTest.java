package escala_plantoes.com.example.demo.domain;

import escala_plantoes.com.example.demo.controller.professional.dto.ProfessionalRegistrationRequestDTO;
import escala_plantoes.com.example.demo.controller.professional.dto.ProfessionalRequestDTO;
import escala_plantoes.com.example.demo.domain.professional.ProfessionalValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ProfessionalValidatorTest {

    private ProfessionalValidator validator;
    private ProfessionalRegistrationRequestDTO validRegistration;

    @BeforeEach
    void setUp() {
        validator = new ProfessionalValidator();
        validRegistration = new ProfessionalRegistrationRequestDTO("MÉDICO", "SP", "CRM", "12345");
    }

    @Test
    void shouldPassWhenAllFieldsAreValid() {
        var dto = new ProfessionalRequestDTO("John Doe", 40, validRegistration);
        assertDoesNotThrow(() -> validator.validate(dto));
    }

    @Test
    void shouldThrowWhenNameIsNull() {
        var dto = new ProfessionalRequestDTO(null, 40, validRegistration);
        var ex = assertThrows(IllegalArgumentException.class, () -> validator.validate(dto));
        assertEquals("Name cannot be null", ex.getMessage());
    }

    @Test
    void shouldThrowWhenRegistrationIsNull() {
        var dto = new ProfessionalRequestDTO("John Doe", 40, null);
        var ex = assertThrows(IllegalArgumentException.class, () -> validator.validate(dto));
        assertEquals("Registration cannot be null", ex.getMessage());
    }

    @Test
    void shouldThrowWhenRegistrationNumberIsNull() {
        var registration = new ProfessionalRegistrationRequestDTO("MÉDICO", "SP", "CRM", null);
        var dto = new ProfessionalRequestDTO("John Doe", 40, registration);
        var ex = assertThrows(IllegalArgumentException.class, () -> validator.validate(dto));
        assertEquals("Registration number cannot be null", ex.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345", "0", "999999"})
    void shouldPassForRegistrationNumberWithOnlyDigits(String registrationNumber) {
        var registration = new ProfessionalRegistrationRequestDTO("MÉDICO", "SP", "CRM", registrationNumber);
        var dto = new ProfessionalRequestDTO("John Doe", 40, registration);
        assertDoesNotThrow(() -> validator.validate(dto));
    }

    @ParameterizedTest
    @ValueSource(strings = {"CRM-123", "12 34", "abc", "12.345"})
    void shouldThrowForRegistrationNumberWithNonDigits(String registrationNumber) {
        var registration = new ProfessionalRegistrationRequestDTO("MÉDICO", "SP", "CRM", registrationNumber);
        var dto = new ProfessionalRequestDTO("John Doe", 40, registration);
        var ex = assertThrows(IllegalArgumentException.class, () -> validator.validate(dto));
        assertEquals("Registration number must contain only digits", ex.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {20, 30, 40})
    void shouldPassForValidWorkSchedules(int workSchedule) {
        var dto = new ProfessionalRequestDTO("John Doe", workSchedule, validRegistration);
        assertDoesNotThrow(() -> validator.validate(dto));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 10, 25, 35, 50})
    void shouldThrowForInvalidWorkSchedules(int workSchedule) {
        var dto = new ProfessionalRequestDTO("John Doe", workSchedule, validRegistration);
        var ex = assertThrows(IllegalArgumentException.class, () -> validator.validate(dto));
        assertEquals("Work schedule must be 20, 30 or 40 hours", ex.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"MÉDICO", "ENFERMEIRO", "TÉCNICO"})
    void shouldPassForValidCategories(String category) {
        var registration = new ProfessionalRegistrationRequestDTO(category, "SP", "CRM", "12345");
        var dto = new ProfessionalRequestDTO("John Doe", 40, registration);
        assertDoesNotThrow(() -> validator.validate(dto));
    }

    @ParameterizedTest
    @ValueSource(strings = {"DOCTOR", "INVALID", "", "medico"})
    void shouldThrowForInvalidCategories(String category) {
        var registration = new ProfessionalRegistrationRequestDTO(category, "SP", "CRM", "12345");
        var dto = new ProfessionalRequestDTO("John Doe", 40, registration);
        var ex = assertThrows(IllegalArgumentException.class, () -> validator.validate(dto));
        assertEquals("Category must be MÉDICO, ENFERMEIRO or TÉCNICO", ex.getMessage());
    }
}
