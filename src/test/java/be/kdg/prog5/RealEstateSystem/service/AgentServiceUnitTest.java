package be.kdg.prog5.RealEstateSystem.service;

import be.kdg.prog5.RealEstateSystem.domain.Agent;
import be.kdg.prog5.RealEstateSystem.domain.exception.NotFoundException;
import be.kdg.prog5.RealEstateSystem.repository.AgentPropertyRepository;
import be.kdg.prog5.RealEstateSystem.repository.AgentRepository;
import be.kdg.prog5.RealEstateSystem.repository.PropertyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.function.Executable;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class AgentServiceUnitTest {
    private AgentService sut;
    private AgentRepository agentRepository;
    private AgentPropertyRepository agentPropertyRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        agentRepository = mock(AgentRepository.class);
        agentPropertyRepository = mock(AgentPropertyRepository.class);
        passwordEncoder = mock(BCryptPasswordEncoder.class);
        PropertyRepository propertyRepository = mock(PropertyRepository.class);

        sut = new AgentService(
                agentRepository,
                passwordEncoder,
                agentPropertyRepository,
                propertyRepository
        );
    }

    @Test
    void shouldGetAgentByIdSuccessfully() {
        // Arrange
        UUID agentId = UUID.randomUUID();
        Agent expectedAgent = new Agent();
        when(agentRepository.findById(agentId)).thenReturn(Optional.of(expectedAgent));

        // Act
        Agent result = sut.findAgentById(agentId);

        // Assert
        assertEquals(expectedAgent, result);
        verify(agentRepository).findById(agentId);
    }

    @Test
    void shouldThrowWhenAgentNotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(agentRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act
        Executable action = () -> sut.findAgentById(nonExistentId);

        // Assert
        assertThrows(NotFoundException.class, action);
        verify(agentRepository).findById(nonExistentId);
    }

    @Test
    void shouldRemoveAgentAndItsProperties() {
        // Arrange
        UUID agentId = UUID.randomUUID();
        Agent agent = new Agent();
        when(agentRepository.findById(agentId)).thenReturn(Optional.of(agent));

        // Act
        sut.remove(agentId);

        // Assert
        verify(agentPropertyRepository).deleteAll(agent.getAgentProperties());
        verify(agentRepository).deleteById(agentId);
    }

    @Test
    void shouldUpdateAgentSuccessfully() {
        // Arrange
        UUID agentId = UUID.randomUUID();
        Agent existingAgent = new Agent();
        existingAgent.setAgentName("Old Name");
        existingAgent.setContactInfo("Old Contact");
        existingAgent.setLicenceNumber("OLD123");
        existingAgent.setEmail("old@example.com");

        when(agentRepository.findById(agentId)).thenReturn(Optional.of(existingAgent));

        // Act
        Agent updatedAgent = sut.updateAgent(
                agentId,
                "New Contact",
                "NEW123",
                "new@example.com"
        );

        // Assert
        assertEquals("New Contact", updatedAgent.getContactInfo());
        assertEquals("NEW123", updatedAgent.getLicenceNumber());
        assertEquals("new@example.com", updatedAgent.getEmail());
    }
}
