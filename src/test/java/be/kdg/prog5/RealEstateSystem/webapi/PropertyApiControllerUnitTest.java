package be.kdg.prog5.RealEstateSystem.webapi;


import be.kdg.prog5.RealEstateSystem.domain.Property;
import be.kdg.prog5.RealEstateSystem.domain.PropertyStatus;
import be.kdg.prog5.RealEstateSystem.domain.PropertyType;
import be.kdg.prog5.RealEstateSystem.domain.Role;
import be.kdg.prog5.RealEstateSystem.security.CustomUserDetails;
import be.kdg.prog5.RealEstateSystem.service.AuthorizationService;
import be.kdg.prog5.RealEstateSystem.service.PropertyService;
import be.kdg.prog5.RealEstateSystem.webapi.dto.PropertyDto;
import be.kdg.prog5.RealEstateSystem.webapi.dto.request.AddPropertyDto;
import be.kdg.prog5.RealEstateSystem.webapi.dto.response.PropertyMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class PropertyApiControllerUnitTest {
    @Autowired
    private PropertyApiController sut;

    @MockitoBean
    private PropertyService propertyService;

    @MockitoBean
    private PropertyMapper propertyMapper;

    @MockitoBean
    private AuthorizationService authorizationService;

    @Test
    void shouldAddPropertySuccessfully() {
        // Arrange
        final AddPropertyDto dto = new AddPropertyDto(
                "Test Property", "Test Address", 250000.0,
                PropertyType.RESIDENTIAL, 100.0, PropertyStatus.AVAILABLE, 3, null
        );

        final CustomUserDetails userDetails = new CustomUserDetails(
                "test@example.com", "password", Role.USER, UUID.randomUUID()
        );

        Property mockProperty = new Property();
        PropertyDto mockDto = new PropertyDto(
                UUID.randomUUID(),
                "Test Property",
                "Test Address",
                250000.0,
                PropertyType.RESIDENTIAL,
                100.0,
                PropertyStatus.AVAILABLE,
                3,
                null,
                null
        );

        when(propertyService.add(
                anyString(), anyString(), anyDouble(), any(),
                anyDouble(), any(), anyInt(), any(), any()
        )).thenReturn(mockProperty);

        when(propertyMapper.toPropertyDto(mockProperty)).thenReturn(mockDto);

        // Act
        final var response = sut.addProperty(dto, userDetails);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Property", response.getBody().propertyName());
        verify(propertyService).add(
                dto.propertyName(), dto.address(), dto.price(), dto.type(),
                dto.size(), dto.status(), dto.numberOfRooms(), dto.dateListed(),
                userDetails.getAgentId()
        );
    }

    @Test
    void shouldDeletePropertySuccessfully() {
        // Arrange
        UUID propertyId = UUID.randomUUID();
        UUID agentId = UUID.randomUUID();
        CustomUserDetails userDetails = new CustomUserDetails(
                "admin@example.com", "password", Role.ADMIN, agentId
        );

        // Mock the authorization service to return true
        when(authorizationService.isModificationAllowed(userDetails, propertyId)).thenReturn(true);

        // Act
        var response = sut.remove(propertyId, userDetails);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(propertyService).remove(propertyId);
        verify(authorizationService).isModificationAllowed(userDetails, propertyId);
    }

    @Test
    void shouldNotDeletePropertyWhenUnauthorized() {
        // Arrange
        UUID propertyId = UUID.randomUUID();
        CustomUserDetails userDetails = new CustomUserDetails(
                "user@example.com", "password", Role.USER, UUID.randomUUID()
        );

        when(authorizationService.isModificationAllowed(userDetails, propertyId))
                .thenReturn(false);

        // Act & Assert
        assertThrows(AccessDeniedException.class, () ->
                sut.remove(propertyId, userDetails));
    }
}
