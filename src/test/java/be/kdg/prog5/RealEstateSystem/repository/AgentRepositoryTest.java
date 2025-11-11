package be.kdg.prog5.RealEstateSystem.repository;


import be.kdg.prog5.RealEstateSystem.TestHelper;
import be.kdg.prog5.RealEstateSystem.domain.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AgentRepositoryTest {
    @Autowired
    private AgentRepository sut;
    @Autowired
    private TestHelper testHelper;

    @Autowired
    private AgentPropertyRepository agentPropertyRepository;

    private Agent testAgent;
    private Property testProperty;


    @BeforeEach
    void setUp() {
        testAgent = testHelper.createAgent();
        testProperty = testHelper.createProperty();
    }

    @Test
    void deletingAgentShouldRemoveAssosiatedProperies(){
        // arrange
        Agent agent = testHelper.createAgent();
        Property property = testHelper.createProperty();
        AgentProperty agentProperty = testHelper.propertyOwnedByAgent(agent, property);
        UUID agentPropertyId = agentProperty.getId();

        // act
        sut.deleteById(agent.getAgentId());

        // assert
        boolean exists = agentPropertyRepository.findById(agentPropertyId).isPresent();
        assertFalse(exists, "agent's property should be deleted along with agent");
    }

    @Test
    void findByEmailShouldReturnAgentWhenEmailExists() {
        // act
        Optional<Agent> foundAgent = sut.findByEmail(testAgent.getEmail());

        // assert
        assertTrue(foundAgent.isPresent(), "Agent should be found by email");
        assertEquals(testAgent.getAgentId(), foundAgent.get().getAgentId());
    }

    @Test
    void findByEmailShouldReturnEmptyWhenEmailDoesNotExist() {
        // act
        Optional<Agent> foundAgent = sut.findByEmail("nonexistent@example.com");

        // assert
        assertFalse(foundAgent.isPresent(), "No agent should be found for non-existent email");
    }

    @Test
    void findByLicenceNumberShouldReturnAgentsWithGivenLicenceNumber() {
        // arrange
        String licenceNumber = testAgent.getLicenceNumber();

        // act
        List<Agent> agents = sut.findByLicenceNumber(licenceNumber);

        // assert
        assertThat(agents).hasSize(1);
        assertEquals(testAgent.getAgentId(), agents.get(0).getAgentId());
    }

    @Test
    void findAgentWithAssignedPropertiesShouldEagerlyLoadProperties() {
        // arrange
        testHelper.propertyOwnedByAgent(testAgent, testProperty);

        // act
        Optional<Agent> agentOptional = sut.findAgentWithAssignedProperties(testAgent.getAgentId());

        // assert
        assertTrue(agentOptional.isPresent());
        Agent agent = agentOptional.get();
        assertNotNull(agent.getAgentProperties(), "Agent properties should be eagerly loaded");
        assertEquals(1, agent.getAgentProperties().size());
    }


    @AfterEach
    void cleanUp() {
        testHelper.cleanUp();
    }



}
