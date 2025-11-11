package be.kdg.prog5.RealEstateSystem.webapi.dto;

import be.kdg.prog5.RealEstateSystem.domain.Agent;

import java.util.UUID;

public record AgentDto(UUID agentId, String agentName, String contactInfo, String licenceNumber, String email) {
    public static AgentDto fromAgent(final Agent agent) {
        return new AgentDto(agent.getAgentId(), agent.getAgentName(), agent.getContactInfo(), agent.getLicenceNumber(), agent.getEmail());
    }
}
