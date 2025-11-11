package be.kdg.prog5.RealEstateSystem.controller.viewmodel;

import be.kdg.prog5.RealEstateSystem.domain.Agent;
import be.kdg.prog5.RealEstateSystem.domain.AgentProperty;
import be.kdg.prog5.RealEstateSystem.domain.Property;

import java.util.List;
import java.util.UUID;

public record AgentsWithManagedPropertiesViewModel(UUID agentId,
                                                   String agentName,
                                                   String contactInfo,
                                                   String licenceNumber,
                                                   String email,
                                                   List<PropertyViewModel> assignedProperties) {

    public static AgentsWithManagedPropertiesViewModel from(final Agent agent) {
        final List<PropertyViewModel> assignedProperties = agent
                .getAgentProperties()
                .stream()
                .map(AgentProperty::getProperty)
                .map((Property property) -> PropertyViewModel.from(property, false))
                .toList();
        return new AgentsWithManagedPropertiesViewModel(agent.getAgentId(), agent.getAgentName(), agent.getContactInfo(), agent.getLicenceNumber(), agent.getEmail(), assignedProperties);
    }

    public boolean hasManagedProperties(){
        return !assignedProperties.isEmpty();
    }
}
