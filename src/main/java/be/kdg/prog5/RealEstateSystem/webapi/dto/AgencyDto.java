package be.kdg.prog5.RealEstateSystem.webapi.dto;

import be.kdg.prog5.RealEstateSystem.domain.RealEstateAgency;

import java.util.UUID;

public record AgencyDto(
        UUID agencyId,
        String agencyName,
        String address,
        String city,
        String contactInfo,
        String image
) {
    public static AgencyDto fromAgency(final RealEstateAgency agency) {
        return new AgencyDto(
                agency.getAgencyId(),
                agency.getAgencyName(),
                agency.getAddress(),
                agency.getCity(),
                agency.getContactInfo(),
                agency.getImage()
        );
    }
}