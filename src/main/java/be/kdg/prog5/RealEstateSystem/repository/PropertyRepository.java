package be.kdg.prog5.RealEstateSystem.repository;

import be.kdg.prog5.RealEstateSystem.domain.Property;
import be.kdg.prog5.RealEstateSystem.domain.PropertyStatus;
import be.kdg.prog5.RealEstateSystem.domain.PropertyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PropertyRepository extends JpaRepository<Property, UUID> {

    @Query("SELECT p FROM Property p " +
            "LEFT JOIN FETCH p.agentProperties ap " +
            "LEFT JOIN FETCH ap.agent")
    List<Property> findAllWithAgents();


    @Query("""
        FROM Property p
        LEFT JOIN FETCH p.agentProperties ap
        LEFT JOIN FETCH ap.agent
        WHERE p.propertyId = :propertyId
""")
    Optional<Property> findPropertiesWithAssigned(@Param("propertyId") UUID propertyId);

    @Query("""
    SELECT p FROM Property p
    LEFT JOIN FETCH p.agentProperties ap
    LEFT JOIN FETCH ap.agent
    WHERE (:minPrice IS NULL OR p.price >= :minPrice)
    AND (:maxPrice IS NULL OR p.price <= :maxPrice)
    AND (:type IS NULL OR p.type = :type)
    AND (:status IS NULL OR p.status = :status)
""")
    List<Property> findFilteredProperties(
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("type") PropertyType type,
            @Param("status") PropertyStatus status);
}
