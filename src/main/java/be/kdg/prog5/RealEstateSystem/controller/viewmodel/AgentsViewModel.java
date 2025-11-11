package be.kdg.prog5.RealEstateSystem.controller.viewmodel;

import be.kdg.prog5.RealEstateSystem.domain.Agent;

import java.util.List;

public record AgentsViewModel(List<AgentViewModel> agents) {
    public static AgentsViewModel from(final List<Agent> agents) {
        return new AgentsViewModel(agents.stream().map(AgentViewModel::from).toList());
    }
}
