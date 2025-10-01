package com.application.climb.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

        // Rotas liberadas 
    private static final String[] PUBLIC_MATCHERS = {
            "/"
    };

    private static final String[] PUBLIC_MATCHERS_POST = {
            
            "/funcionario/login",  "/chamado/create" 
            
    };

    private static final String[] PUBLIC_MATCHERS_GET = {
        "/funcionario/GetFuncionario"
        }; 
        

        // Filtro para permitir todas as rotas  e metodos 
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults()) //
                .authorizeHttpRequests(auth -> auth
                .requestMatchers(PUBLIC_MATCHERS).permitAll()
                .requestMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
                .requestMatchers(PUBLIC_MATCHERS_POST).permitAll()
                .anyRequest().authenticated()

                );

        return http.build();
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;

    }
    
    

}