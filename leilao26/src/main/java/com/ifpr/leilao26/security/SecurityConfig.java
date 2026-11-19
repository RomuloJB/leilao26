package com.ifpr.leilao26.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public AuthenticationProvider authenticationProvider (){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // ajustar para a url do front em producao
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:8080"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // /auth/me precisa vir antes do /auth/** (a primeira regra que casa é a que vale)
                .requestMatchers(HttpMethod.GET, "/auth/me").authenticated()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/pessoa/registrar").permitAll()
                .requestMatchers(HttpMethod.GET, "/leilao/buscar/**").permitAll()
                //
                .requestMatchers(HttpMethod.POST, "/leilao/registrar").hasAnyRole("COMPRADOR", "VENDEDOR", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/leilao/atualizar/**").hasAnyRole("COMPRADOR", "VENDEDOR", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/leilao/excluir/**").hasAnyRole("COMPRADOR", "VENDEDOR", "ADMIN")
                //
                .requestMatchers(HttpMethod.POST, "/lance/registrar").hasAnyRole("COMPRADOR", "VENDEDOR")
                .requestMatchers(HttpMethod.GET, "/lance/buscar/leilao/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/lance/excluir/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/categoria/buscar/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/uploads/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/imagem/arquivo/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/categoria/buscar/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/categoria/registrar").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/categoria/atualizar/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/categoria/excluir/**").hasRole("ADMIN")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
