package com.mfc.infra.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@Configuration
public class ArqSecurityConfig {

    private AuthenticationProvider authenticationProvider;
    //private final LogoutHandler logoutHandler;
    //private final AuthenticationEntryPoint authenticationEntryPoint;
    //private final AccessDeniedHandler accessDeniedHandler;
    public ArqSecurityConfig(AuthenticationProvider authenticationProvider/*,
                          UsernamePasswordAuthenticationFilter jwtAuthenticationFilter,
                          LogoutHandler logoutHandler, AuthenticationEntryPoint authenticationEntryPoint,
                          AccessDeniedHandler accessDeniedHandler*/) {
        this.authenticationProvider = authenticationProvider;
        // this.logoutHandler = logoutHandler;
        //this.authenticationEntryPoint = authenticationEntryPoint;
        //this.accessDeniedHandler = accessDeniedHandler;
    }

    private static final AntPathRequestMatcher[] WHITELIST = {
            AntPathRequestMatcher.antMatcher("/auditorias/**"),
            AntPathRequestMatcher.antMatcher("/customer*/**"),
            AntPathRequestMatcher.antMatcher("/diplomas*/**"),
            AntPathRequestMatcher.antMatcher("/regalo/**"),
            AntPathRequestMatcher.antMatcher("/auth/**"),
            AntPathRequestMatcher.antMatcher("/v2/api-docs"),
            AntPathRequestMatcher.antMatcher("/v3/api-docs"),
            AntPathRequestMatcher.antMatcher("/v3/api-docs/**"),
            AntPathRequestMatcher.antMatcher("/swagger-resources"),
            AntPathRequestMatcher.antMatcher("/swagger-resources/**"),
            AntPathRequestMatcher.antMatcher("/configuration/ui"),
            AntPathRequestMatcher.antMatcher("/configuration/security"),
            AntPathRequestMatcher.antMatcher("/swagger-ui/**"),
            AntPathRequestMatcher.antMatcher("/webjars/**"),
            AntPathRequestMatcher.antMatcher("/swagger-ui.html")
    };


    /**
     * this method provides a filter that intercepts request and authenticate the user.
     * this also provides a mechanism to ignore some endpoints from security procedure.
     * @param  httpSecurity this extends AbstractConfiguredSecurityBuilder<DefaultSecurityFilterChain, HttpSecurity>
     * @return securityFilterChain
     * @see ArqSecurityConfig
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        //var managerRole = AuthorityAuthorizationManager.<RequestAuthorizationContext>hasRole("muface");
        //managerRole.setRoleHierarchy(roleHierarchy());
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITELIST).permitAll()
                        //.requestMatchers(AntPathRequestMatcher.antMatcher("/customer/**")).hasRole("muface")
                        //.requestMatchers(AntPathRequestMatcher.antMatcher("/diploma/**")).access(managerRole)
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider)
                //.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout.logoutUrl("/customer/logout")
                        //.addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()))
                //.exceptionHandling(exc -> exc
                //      .authenticationEntryPoint(authenticationEntryPoint)
                //      .accessDeniedHandler(accessDeniedHandler)
                //)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS).maximumSessions(1)).build();
    }


    /**
     * Exposing a bean of type RoleHierarchy :
     * We’ve configured the role ADMIN to include the role MANAGER, which in turn includes the role USER.
     * @return hierarchy RoleHierarchy.
     * @see ArqSecurityConfig
     */
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_ADMIN > ROLE_MANAGER");
        return hierarchy;
    }
}
