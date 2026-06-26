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

                        .requestMatchers("/login", "/css/**", "/js/**").permitAll()

                        // =========================
                        // GESTION PAGE (WORKFLOW)
                        // =========================
                        .requestMatchers("/cautions/gestion")
                        .hasAnyRole("GESTIONNAIRE", "ADMINISTRATEUR")

                        // =========================
                        // CREATE CAUTION
                        // =========================
                        .requestMatchers("/cautions/new", "/cautions/save")
                        .hasAnyRole("GESTIONNAIRE", "ADMINISTRATEUR")

                        // =========================
                        // CHANGE STATE
                        // =========================
                        .requestMatchers("/cautions/*/state")
                        .hasAnyRole("GESTIONNAIRE", "ADMINISTRATEUR")

                        // =========================
                        // EDIT + DELETE (ADMIN ONLY)
                        // =========================
                        .requestMatchers("/cautions/edit/**", "/cautions/delete/**")
                        .hasRole("ADMINISTRATEUR")

                        // =========================
                        // ADMIN MODULES
                        // =========================
                        .requestMatchers("/users/**", "/banques/**", "/fournisseurs/**", "/ordonnateurs/**")
                        .hasRole("ADMINISTRATEUR")
                        .requestMatchers("/cautions/admin/**")
                        .hasRole("ADMINISTRATEUR")
                        // DEFAULT
                        .anyRequest().authenticated()
                )

                // ==================================================
                // LOGIN CONFIG
                // ==================================================
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("matricule")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )

                // ==================================================
                // LOGOUT CONFIG
                // ==================================================
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                )

                // ==================================================
                // USER DETAILS SERVICE
                // ==================================================
                .userDetailsService(userDetailsService);

        return http.build();
    }

    // ======================================================
    // PASSWORD ENCODER
    // ======================================================
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ======================================================
    // AUTH MANAGER
    // ======================================================
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }
}