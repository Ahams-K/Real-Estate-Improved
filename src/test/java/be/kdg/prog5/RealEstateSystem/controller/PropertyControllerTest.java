package be.kdg.prog5.RealEstateSystem.controller;
import be.kdg.prog5.RealEstateSystem.TestHelper;
import be.kdg.prog5.RealEstateSystem.controller.viewmodel.PropertiesViewModel;
import be.kdg.prog5.RealEstateSystem.controller.viewmodel.PropertyViewModel;
import be.kdg.prog5.RealEstateSystem.domain.Property;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.util.UUID;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class PropertyControllerTest {
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
    @WithUserDetails(value = "gloria@example.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldShowProperties() throws Exception {
        final var request = get("/properties");

        final var result = mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("properties"))
                .andExpect(model().attribute("Properties", instanceOf(PropertiesViewModel.class)))
                .andReturn();

        final var model = result.getModelAndView().getModel();
        final var properties = (PropertiesViewModel) model.get("Properties");
        assertEquals(1, properties.getAmount());
        assertEquals(property.getPropertyId(), properties.properties().get(0).propertyId());
    }

    @Test
    @WithUserDetails(value = "gloria@example.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldShowPropertyDetails() throws Exception {
        final var request = get("/properties/" + property.getPropertyId());

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("property-details"))
                .andExpect(model().attributeExists("property"))
                .andExpect(model().attribute("property", instanceOf(PropertyViewModel.class)));
    }
}