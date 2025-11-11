package be.kdg.prog5.RealEstateSystem.webapi;

import be.kdg.prog5.RealEstateSystem.TestHelper;
import be.kdg.prog5.RealEstateSystem.domain.Property;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.util.UUID;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class PropertyApiControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestHelper testHelper;

    private Property property;
    private UUID agentId;

    @BeforeEach
    void setUp() {
        testHelper.cleanUp();
        var agent = testHelper.createAgent();
        agentId = agent.getAgentId();
        property = testHelper.createProperty();
        testHelper.propertyOwnedByAgent(agent, property);
    }

    @Test
    void shouldGetAgentsForProperty() throws Exception {
        // Arrange
        final var request = get("/api/properties/" + property.getPropertyId() + "/agents");

        // Act & Assert
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].agentId").value(agentId.toString()));
    }

    @Test
    @WithUserDetails(value = "gloria@example.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldAddProperty() throws Exception {
        // Arrange
        final var request = post("/api/properties")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "propertyName": "Villa",
                    "address": "123 Main St",
                    "price": 350000.0,
                    "type": "RESIDENTIAL",
                    "size": 150.0,
                    "status": "AVAILABLE",
                    "numberOfRooms": 4,
                    "dateListed": "2023-01-01"
                }
                """);

        // Act & Assert
        final String jsonResponse = mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.propertyName").value("Villa"))
                .andExpect(jsonPath("$.price").value(350000.0))
                .andReturn()
                .getResponse()
                .getContentAsString();

        final UUID createdPropertyId = UUID.fromString(JsonPath.read(jsonResponse, "$.propertyId"));

        // Verify the property was actually created
        mockMvc.perform(get("/api/properties/" + createdPropertyId + "/agents"))
                .andExpect(jsonPath("$[0].agentId").value(agentId.toString()));
    }

    @Test
    @WithUserDetails(value = "gloria@example.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldDeletePropertyAsOwner() throws Exception {
        // Arrange
        final var request = delete("/api/properties/" + property.getPropertyId())
                .with(csrf());

        // Act & Assert
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        // Verify the property was actually deleted
        mockMvc.perform(get("/api/properties/" + property.getPropertyId() + "/agents"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldNotDeletePropertyWhenUnauthenticated() throws Exception {
        // Arrange
        final var request = delete("/api/properties/" + property.getPropertyId())
                .with(csrf());

        // Act & Assert
        mockMvc.perform(request)
                .andExpect(status().isUnauthorized());
    }
}