package be.kdg.prog5.RealEstateSystem.webapi.dto.response;

import be.kdg.prog5.RealEstateSystem.domain.Agent;
import be.kdg.prog5.RealEstateSystem.webapi.dto.AgentDto;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface AgentMapper {
    AgentDto toAgentDto(Agent agent);
}
