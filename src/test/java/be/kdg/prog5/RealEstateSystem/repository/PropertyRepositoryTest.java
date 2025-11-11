package be.kdg.prog5.RealEstateSystem.repository;

import be.kdg.prog5.RealEstateSystem.TestHelper;
import be.kdg.prog5.RealEstateSystem.domain.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class PropertyRepositoryTest {
    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    private AgentPropertyRepository agentPropertyRepository;
    @Autowired
    private TestHelper testHelper;

    private Property savedProperty;

    @BeforeEach
    void setUp() {
        savedProperty = testHelper.createProperty();
    }

    @Test
    void findAllWithAgentsShouldReturnPropertiesWithAgents() {
        // Arrange
        Agent agent = testHelper.createAgent();
        testHelper.propertyOwnedByAgent(agent, savedProperty);

        // Act
        List<Property> properties = propertyRepository.findAllWithAgents();

        // Assert
        assertThat(properties).isNotEmpty();
        assertThat(properties.get(0).getAgentProperties()).isNotEmpty();
    }

    @Test
    void findPropertiesWithAssignedShouldEagerLoadAssociations() {
        // Arrange
        Agent agent = testHelper.createAgent();
        testHelper.propertyOwnedByAgent(agent, savedProperty);

        // Act
        Optional<Property> property = propertyRepository.findPropertiesWithAssigned(savedProperty.getPropertyId());

        // Assert
        assertTrue(property.isPresent());
        assertFalse(property.get().getAgentProperties().isEmpty());
        assertNotNull(property.get().getAgentProperties().get(0).getAgent());
    }

    @Test
    void findFilteredPropertiesShouldApplyAllFilters() {
        // Arrange
        Property commercialProp = new Property();
        commercialProp.setType(PropertyType.COMMERCIAL);
        commercialProp.setPrice(500000.0);
        commercialProp.setStatus(PropertyStatus.PENDING);
        propertyRepository.save(commercialProp);

        // Act
        List<Property> residential = propertyRepository.findFilteredProperties(
                200000.0, 300000.0, PropertyType.RESIDENTIAL, null);
        List<Property> commercial = propertyRepository.findFilteredProperties(
                400000.0, null, PropertyType.COMMERCIAL, null);
        List<Property> pending = propertyRepository.findFilteredProperties(
                null, null, null, PropertyStatus.PENDING);

        // Assert
        assertEquals(1, residential.size());
        assertEquals(savedProperty.getPropertyId(), residential.get(0).getPropertyId());
        assertEquals(1, commercial.size());
        assertEquals(commercialProp.getPropertyId(), commercial.get(0).getPropertyId());
        assertEquals(1, pending.size());
    }

    @Test
    void saveShouldPersistProperty() {
        // Arrange
        Property newProperty = new Property();
        newProperty.setPropertyName("New Test Property");
        newProperty.setPrice(275000.0);

        // Act
        Property saved = propertyRepository.save(newProperty);

        // Assert
        assertNotNull(saved.getPropertyId());
        Optional<Property> found = propertyRepository.findById(saved.getPropertyId());
        assertTrue(found.isPresent());
        assertEquals("New Test Property", found.get().getPropertyName());
    }


    @AfterEach
    void cleanUp() {
        testHelper.cleanUp();
    }

}
