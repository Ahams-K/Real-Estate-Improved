package be.kdg.prog5.RealEstateSystem.repository;

import aj.org.objectweb.asm.commons.Remapper;
import be.kdg.prog5.RealEstateSystem.domain.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgentRepository extends JpaRepository<Agent, UUID> {


    List<Agent> findByLicenceNumber(String licenceNumber);

    @Query("SELECT a FROM Agent a " +
            "WHERE a.agentName = :identifier " +
            "OR a.email = :identifier")
    Optional<Agent> findByIdentifier(@Param("identifier") String identifier);


    @Query("""
        FROM Agent a
        LEFT JOIN FETCH a.agentProperties ag
        LEFT JOIN FETCH ag.agent
        """)
    List<Agent> findAgents();

    @Query("""
        SELECT a
        FROM Agent a
        LEFT JOIN FETCH a.agentProperties ap
        LEFT JOIN FETCH ap.property
        WHERE a.agentId = :agentId
        """)
    Optional<Agent> findAgentWithAssignedProperties(@Param("agentId") UUID agentId);


    Optional<Agent> findByEmail(String username);
}
