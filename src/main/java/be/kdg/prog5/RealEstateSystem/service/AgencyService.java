package be.kdg.prog5.RealEstateSystem.service;

import be.kdg.prog5.RealEstateSystem.domain.RealEstateAgency;
import be.kdg.prog5.RealEstateSystem.repository.AgencyRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AgencyService implements AgencyServiceInterface {
    private final AgencyRepository agencyRepository;

    public AgencyService(AgencyRepository agencyRepository) {
        this.agencyRepository = agencyRepository;
    }

    @Override
    public List<RealEstateAgency> getAll() {
        return agencyRepository.findAll();
    }

    @Override
    public Optional<RealEstateAgency> findAgencyById(UUID agencyId) {
        return agencyRepository.findAgencyWithDetailsById(agencyId);
    }

    public RealEstateAgency add(RealEstateAgency agency) {
        return agencyRepository.save(agency);
    }

}
