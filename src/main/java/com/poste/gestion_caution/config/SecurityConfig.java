package com.poste.gestion_caution.config;

import com.poste.gestion_caution.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth

                        // PUBLIC
                        .requestMatchers("/login", "/css/**", "/js/**").permitAll()

                        // =========================
                        // CAUTIONS (MAIN MODULE)
                        // =========================

                        // LIST ONLY (CONSULTEUR + others)
                        .requestMatchers("/cautions")
                        .hasAnyRole("CONSULTEUR", "GESTIONNAIRE", "ADMINISTRATEUR")

                        // ACTIONS (NO CONSULTEUR)
                        .requestMatchers(
                                "/cautions/new",
                                "/cautions/create",
                                "/cautions/edit/**",
                                "/cautions/update",
                                "/cautions/delete/**"
                        )
                        .hasAnyRole("GESTIONNAIRE", "ADMINISTRATEUR")

                        // =========================
                        // ADMIN MODULES ONLY
                        // =========================
                        .requestMatchers(
                                "/users/**",
                                "/banques/**",
                                "/fournisseurs/**",
                                "/ordonnateurs/**"
                        )
                        .hasRole("ADMINISTRATEUR")

                        // EVERYTHING ELSE
                        .anyRequest().authenticated()
                )

                // LOGIN CONFIG
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("matricule")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )

                // LOGOUT CONFIG
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                )

                // USER DETAILS
                .userDetailsService(userDetailsService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}