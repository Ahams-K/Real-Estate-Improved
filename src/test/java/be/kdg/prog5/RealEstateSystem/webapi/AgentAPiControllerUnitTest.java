package be.kdg.prog5.RealEstateSystem.webapi;

import be.kdg.prog5.RealEstateSystem.domain.Agent;
import be.kdg.prog5.RealEstateSystem.domain.Role;
import be.kdg.prog5.RealEstateSystem.domain.exception.NotFoundException;
import be.kdg.prog5.RealEstateSystem.service.AgentService;
import be.kdg.prog5.RealEstateSystem.service.AuthorizationService;
import be.kdg.prog5.RealEstateSystem.webapi.dto.AgentDto;
import be.kdg.prog5.RealEstateSystem.webapi.dto.PropertyDto;
import be.kdg.prog5.RealEstateSystem.webapi.dto.request.UpdateAgentDto;
import be.kdg.prog5.RealEstateSystem.webapi.dto.response.AgentMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
@ActiveProfiles("test")
class AgentAPiControllerUnitTest {
    @Autowired
    private AgentApiController sut;
    @MockitoBean
    private AgentService agentService;
    @MockitoBean
    private AgentMapper agentMapper;
    @MockitoBean
    private AuthorizationService authorizationService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateAgentSuccessfully() {
        // Arrange
        UUID agentId = UUID.randomUUID();
        UpdateAgentDto dto = new UpdateAgentDto("new-contact", "new-licence", "new@email.com");

        Agent agent = new Agent();
        agent.setAgentId(agentId);
        agent.setAgentName("Agent Name");
        agent.setContactInfo(dto.contactInfo());
        agent.setLicenceNumber(dto.licenceNumber());
        agent.setEmail(dto.email());

        when(agentService.findAgentById(agentId)).thenReturn(agent);
        when(agentService.updateAgent(
                agentId,
                dto.contactInfo(),
                dto.licenceNumber(),
                dto.email()
        )).thenReturn(agent);
        when(authorizationService.isAgentModificationAllowed(any(), any())).thenReturn(true);

        // Mock the mapper to return a DTO with fields in the correct order
        when(agentMapper.toAgentDto(agent)).thenReturn(
                new AgentDto(
                        agentId,
                        "Agent Name",
                        "new-contact",
                        "new-licence",
                        "new@email.com"
                )
        );

        // Act
        ResponseEntity<AgentDto> response = sut.updateAgent(agentId, dto, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Verify field mappings according to AgentDto record structure
        assertEquals(agentId, response.getBody().agentId());
        assertEquals("Agent Name", response.getBody().agentName());
        assertEquals("new-contact", response.getBody().contactInfo());
        assertEquals("new-licence", response.getBody().licenceNumber());
        assertEquals("new@email.com", response.getBody().email());

        verify(agentService).updateAgent(
                agentId,
                dto.contactInfo(),
                dto.licenceNumber(),
                dto.email()
        );
    }

    @Test
    @WithMockUser
    void shouldReturnForbiddenWhenUpdatingOtherAgent() {
        // Arrange
        UUID agentId = UUID.randomUUID();
        UpdateAgentDto dto = new UpdateAgentDto("new-contact", "new-licence", "new@email.com");

        Agent agent = new Agent();
        agent.setAgentId(agentId);

        when(agentService.findAgentById(agentId)).thenReturn(agent);
        when(authorizationService.isAgentModificationAllowed(any(), any())).thenReturn(false);

        // Act
        ResponseEntity<AgentDto> response = sut.updateAgent(agentId, dto, null);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }


    @Test
    void shouldGetAgentPropertiesSuccessfully() {
        // Arrange
        UUID agentId = UUID.randomUUID();
        when(agentService.getProperties(agentId)).thenReturn(List.of());

        // Act
        ResponseEntity<List<PropertyDto>> response = sut.getProperties(agentId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(agentService).getProperties(agentId);
    }
}
