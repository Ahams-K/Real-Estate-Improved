package be.kdg.prog5.RealEstateSystem.service;

import be.kdg.prog5.RealEstateSystem.domain.RealEstateAgency;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AgencyServiceInterface {
    List<RealEstateAgency> getAll();
    Optional<RealEstateAgency> findAgencyById(UUID agencyId);
}
