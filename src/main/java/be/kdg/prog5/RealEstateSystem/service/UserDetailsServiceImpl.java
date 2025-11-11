package be.kdg.prog5.RealEstateSystem.service;

import be.kdg.prog5.RealEstateSystem.repository.AgentRepository;
import be.kdg.prog5.RealEstateSystem.security.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final AgentRepository agentRepository;

    public UserDetailsServiceImpl(final AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return agentRepository.findByIdentifier(username)
                .map(agent -> {
                    logger.info("Found agent {}", username);
                    return new CustomUserDetails(
                            agent.getAgentName(),
                            agent.getPassword(),
                            agent.getRole(),
                            agent.getAgentId());
                })
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}

