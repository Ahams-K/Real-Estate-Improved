package be.kdg.prog5.RealEstateSystem.service;

import be.kdg.prog5.RealEstateSystem.domain.Agent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AgentServiceInterface {
    List<Agent> getALL();
    Agent findAgentById(UUID agentId);
    Agent add(final String agentName, final String password, final String contactInfo, final String licenceNumber, final String email, final List<UUID> agentPropertiesIds);
    Agent updateAgent(UUID agentId, String contactInfo, String licenceNumber, String email);

    }
