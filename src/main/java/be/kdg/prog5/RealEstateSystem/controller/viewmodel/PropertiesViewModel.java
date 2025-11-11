package be.kdg.prog5.RealEstateSystem.controller.viewmodel;

import be.kdg.prog5.RealEstateSystem.domain.AgentProperty;
import be.kdg.prog5.RealEstateSystem.domain.Property;

import java.util.List;
import java.util.UUID;

public record PropertiesViewModel(List<PropertiesWithAssignedAgentsViewModel> properties) {
    public int getAmount() {
        return properties.size();
    }


}
