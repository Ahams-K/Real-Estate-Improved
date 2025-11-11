package be.kdg.prog5.RealEstateSystem.security;

import be.kdg.prog5.RealEstateSystem.domain.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
public class CustomUserDetails extends User {


    private final UUID agentId;
    private final Role role;

    //    public CustomUserDetails(String username, String password, final UUID agentId) {
//        super(username, password, Collections.emptyList());
//        this.agentId = agentId;
//    }
    public CustomUserDetails(
            final String username,
            final String password,
            final Role role,
            final UUID agentId
    ){
        super(username, password, List.of(new SimpleGrantedAuthority(
                "ROLE_" + role.toString())));
        this.agentId = agentId;
        this.role = role;
    }

}
