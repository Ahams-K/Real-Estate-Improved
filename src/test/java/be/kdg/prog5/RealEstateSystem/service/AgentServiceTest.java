package be.kdg.prog5.RealEstateSystem.service;

import be.kdg.prog5.RealEstateSystem.TestHelper;
import be.kdg.prog5.RealEstateSystem.domain.Agent;
import be.kdg.prog5.RealEstateSystem.domain.AgentProperty;
import be.kdg.prog5.RealEstateSystem.domain.Property;
import be.kdg.prog5.RealEstateSystem.domain.exception.NotFoundException;
import be.kdg.prog5.RealEstateSystem.repository.AgentPropertyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
class AgentServiceTest {
    @Autowired
    private AgentService sut; // System Under Test
    @Autowired
    private TestHelper testHelper;
    @Autowired
    private AgentPropertyRepository agentPropertyRepository; // For verification

    private Agent testAgent;
    private Property testProperty;

    @BeforeEach
    void setUp() {
        testAgent = testHelper.createAgent();
        testProperty = testHelper.createProperty();
        testHelper.propertyOwnedByAgent(testAgent, testProperty);
    }

    @AfterEach
    void tearDown() {
        testHelper.cleanUp();
    }

    // Test 1: remove() - tests cascading deletion behavior
    @Test
    void removeShouldDeleteAgentAndAssociatedProperties() {
        // Arrange
        UUID agentId = testAgent.getAgentId();

        // Verify setup - agent has one property
        List<AgentProperty> initialProperties = agentPropertyRepository.findAllByAgentId(agentId);
        assertEquals(1, initialProperties.size());

        // Act
        sut.remove(agentId);

        // Assert
        // Verify agent is deleted
        assertThrows(NotFoundException.class, () -> sut.findAgentById(agentId));

        // Verify associated properties are deleted (integration with repository)
        List<AgentProperty> remainingProperties = agentPropertyRepository.findAllByAgentId(agentId);
        assertTrue(remainingProperties.isEmpty());
    }

    // Test 2: add() - tests complex creation with property assignment
    @Test
    void addShouldCreateAgentWithProperties() {
        // Arrange
        String agentName = "Integration Test Agent";
        String password = "secure123";
        String contactInfo = "+32456789123";
        String licenceNumber = "INT-TEST-1";
        String email = "integration@test.com";
        Property newProperty = testHelper.createProperty();

        // Act
        Agent result = sut.add(agentName, password, contactInfo, licenceNumber, email, List.of(testProperty.getPropertyId(), newProperty.getPropertyId()));

        // Assert
        // Verify agent creation
        assertNotNull(result.getAgentId());
        assertEquals(agentName, result.getAgentName());

        // Verify property assignments (integration with repository)
        List<Property> assignedProperties = sut.getProperties(result.getAgentId());
        assertEquals(2, assignedProperties.size());
        assertTrue(assignedProperties.stream().anyMatch(p -> p.getPropertyId().equals(testProperty.getPropertyId())));
        assertTrue(assignedProperties.stream().anyMatch(p -> p.getPropertyId().equals(newProperty.getPropertyId())));
    }

    // Negative test case for remove()
    @Test
    void removeShouldThrowWhenAgentNotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();

        // Act & Assert
        assertThrows(NotFoundException.class, () -> sut.remove(nonExistentId));
    }

    // Negative test case for add()
    @Test
    void addShouldThrowWhenPropertyNotFound() {
        // Arrange
        UUID nonExistentPropertyId = UUID.randomUUID();

        // Act & Assert
        assertThrows(NotFoundException.class, () -> sut.add("Test", "pass", "contact", "lic", "email", List.of(nonExistentPropertyId)));
    }

}

