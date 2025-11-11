package be.kdg.prog5.RealEstateSystem.repository;

import be.kdg.prog5.RealEstateSystem.domain.RealEstateAgency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgencyRepository extends JpaRepository<RealEstateAgency, UUID> {


    @Query("SELECT DISTINCT a FROM RealEstateAgency a " +
            "LEFT JOIN FETCH a.agents " + // Eagerly fetch agents
            "WHERE a.agencyId = :agencyId")
    Optional<RealEstateAgency> findAgencyWithDetailsById(@Param("agencyId") UUID agencyId);
}
