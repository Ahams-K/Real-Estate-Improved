package be.kdg.prog5.RealEstateSystem.controller.viewmodel;

import be.kdg.prog5.RealEstateSystem.domain.Agent;

import java.util.UUID;

public record AgentViewModel(UUID agentId, String agentName, String contactInfo, String licenceNumber, String email) {
    public static AgentViewModel from(final Agent agent) {
        return new AgentViewModel(agent.getAgentId(), agent.getAgentName(), agent.getContactInfo(), agent.getLicenceNumber(), agent.getEmail());
    }
}
