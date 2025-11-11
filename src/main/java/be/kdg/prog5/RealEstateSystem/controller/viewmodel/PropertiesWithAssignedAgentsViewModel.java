package be.kdg.prog5.RealEstateSystem.controller.viewmodel;

import be.kdg.prog5.RealEstateSystem.domain.AgentProperty;
import be.kdg.prog5.RealEstateSystem.domain.Property;
import be.kdg.prog5.RealEstateSystem.domain.PropertyStatus;
import be.kdg.prog5.RealEstateSystem.domain.PropertyType;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record PropertiesWithAssignedAgentsViewModel(UUID propertyId,
                                                    String propertyName,
                                                    String address,
                                                    double price,
                                                    PropertyType type,
                                                    double size,
                                                    PropertyStatus status,
                                                    int numberOfRooms,
                                                    LocalDate dateListed,
                                                    String image,
                                                    List<AgentViewModel> managingAgents,
                                                    boolean isModificationAllowed) {

    public static PropertiesWithAssignedAgentsViewModel from (final Property property, final boolean isModificationAllowed){
        final List<AgentViewModel> managingAgents = property
                .getAgentProperties()
                .stream()
                .map(AgentProperty::getAgent)
                .map(AgentViewModel::from)
                .toList();
        return new PropertiesWithAssignedAgentsViewModel(
                property.getPropertyId(),
                property.getPropertyName(),
                property.getAddress(),
                property.getPrice(),
                property.getType(),
                property.getSize(),
                property.getStatus(),
                property.getNumberOfRooms(),
                property.getDateListed(),
                property.getImage(),
                managingAgents,
                isModificationAllowed);
    }

}
