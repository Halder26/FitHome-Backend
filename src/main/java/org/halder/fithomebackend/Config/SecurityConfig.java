package org.halder.fithomebackend.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authRequest ->
                        authRequest
                                //Public endpoints
                                .requestMatchers("/api/v1/auth/*").permitAll()
                                .requestMatchers("/api/v1/exercises/muscle-groups").permitAll()
                                .requestMatchers("/api/v1/users/genders").permitAll()
                                .requestMatchers("/api/v1/user/image/**").permitAll()
                                .requestMatchers("/api/v1/exercises/image/**").permitAll()
                                .requestMatchers("/api/v1/routines/image/**").permitAll()
                                .requestMatchers("/api/v1/exercises").permitAll()
                                .requestMatchers("/api/v1/routines/public").permitAll()
                                .requestMatchers("/api/v1/auth/update").authenticated()

                                //Admin endpoints
                                .requestMatchers(HttpMethod.POST,"/api/v1/exercise").hasAnyAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.PUT,"/api/v1/exercise/**").hasAnyAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.POST,"/api/v1/routine").hasAnyAuthority("ROLE_ADMIN","ROLE_USER")
//                                .requestMatchers(HttpMethod.PUT,"/api/v1/exercise/**/routine/**").hasAnyAuthority("ROLE_ADMIN","ROLE_USER")
//                                .requestMatchers(HttpMethod.DELETE,"/api/v1/exercise/**/routine/**").hasAnyAuthority("ROLE_ADMIN","ROLE_USER")
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManager->
                        sessionManager
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(Customizer.withDefaults())
                .build();
    }

}
