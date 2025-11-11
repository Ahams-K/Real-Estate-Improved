package be.kdg.prog5.RealEstateSystem.repository;

import be.kdg.prog5.RealEstateSystem.domain.AgentProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AgentPropertyRepository extends JpaRepository<AgentProperty, UUID> {

    @Query("SELECT ap FROM AgentProperty ap " +
            "WHERE ap.agent.agentId = :agentId")
    List<AgentProperty> findAllByAgentId(@Param("agentId") UUID agentId);
}
