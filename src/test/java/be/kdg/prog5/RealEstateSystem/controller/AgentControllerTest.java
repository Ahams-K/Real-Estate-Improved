package be.kdg.prog5.RealEstateSystem.controller;

import be.kdg.prog5.RealEstateSystem.domain.Property;
import be.kdg.prog5.RealEstateSystem.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import be.kdg.prog5.RealEstateSystem.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AgentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestHelper testHelper;

    private Property testProperty;

    @BeforeEach
    void setUp() {
        testHelper.cleanUp();
        testHelper.createAgent("gloria@example.com", Role.USER);
        testHelper.createAgent("admin@example.com", Role.ADMIN);
        testProperty = testHelper.createProperty();
    }

    @Test
    void shouldShowAgents() throws Exception {
        final var request = get("/agents");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("agents"))
                .andExpect(model().attributeExists("Agents"));

    }

    @Test
    @WithUserDetails(value = "admin@example.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldAddAgentAsAdmin() throws Exception {
        final var request = post("/agents/add")
                .with(csrf())
                .param("agentName", "New Agent")
                .param("email", "newagent@example.com")
                .param("password", "password")
                .param("contactInfo", "+320000001")
                .param("licenceNumber", "NON-17E")
                .param("agentProperties", testProperty.getPropertyId().toString());

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/agents"));
    }

    @Test
    @WithUserDetails(value = "gloria@example.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldNotAddAgentAsNonAdmin() throws Exception {
        final var request = post("/agents/add")
                .with(csrf())
                .param("agentName", "New Agent")
                .param("email", "newagent@example.com")
                .param("password", "password")
                .param("contactInfo", "+320000001")
                .param("licenceNumber", "NON-17E")
                .param("agentProperties", testProperty.getPropertyId().toString());

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}