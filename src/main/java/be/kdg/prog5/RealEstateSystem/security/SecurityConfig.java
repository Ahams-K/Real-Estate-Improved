package be.kdg.prog5.RealEstateSystem.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;
import static org.springframework.security.web.util.matcher.RegexRequestMatcher.regexMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(
            final HttpSecurity security) throws Exception {
        return security
                .cors(cors -> cors.configure(security))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(HttpMethod.GET, "/").permitAll()
                            .requestMatchers(HttpMethod.GET, "/agents").permitAll()
                            .requestMatchers(HttpMethod.GET, "/agencies").permitAll()
                            .requestMatchers(HttpMethod.GET, "/properties").permitAll()
                            .requestMatchers(HttpMethod.GET, "/properties/{propertyId:[0-9a-fA-F-]{36}}").permitAll()
                            .requestMatchers(HttpMethod.GET, "/agents/{agentId:[0-9a-fA-F-]{36}}").permitAll()
                            .requestMatchers(HttpMethod.GET, "/agencies/{agencyId:[0-9a-fA-F-]{36}}").permitAll()
                            .requestMatchers(HttpMethod.POST, "/properties/filter").permitAll()
                            .requestMatchers(HttpMethod.GET, "/js/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/webjars/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/css/**", "/images/**").permitAll()
                            .requestMatchers(
                                    regexMatcher(HttpMethod.GET, "/agents/\\d+")
                            ).permitAll()
                            .requestMatchers(
                                    antMatcher(HttpMethod.GET, "/api/agents"),
                                    antMatcher(HttpMethod.GET, "/api/properties"),
                                    antMatcher(HttpMethod.GET, "/api/properties/**/agents"),
                                    antMatcher(HttpMethod.GET, "/api/agents/**/properties")
                            ).permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/agencies/add").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/agencies/search").permitAll()
                            .anyRequest().authenticated();
                })
//                .csrf(csrf -> csrf.disable())
                .exceptionHandling(handler -> {
                    handler.authenticationEntryPoint((request, response, authException) -> {
                        if (request.getRequestURI().startsWith("/api")){
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.getWriter().print(
                                    "You are not allowed beyond this point"
                            );
                        } else {
                            response.sendRedirect("/login");
                        }
                    });
                })
                .formLogin(login -> {
                    login.loginPage("/login")
                            .permitAll()
                            .defaultSuccessUrl("/", true);
                })
                .csrf(csrf -> {
                    csrf.ignoringRequestMatchers("/api/agencies/**");
                })
                .build();

    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
