package com.application.climb.Config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    // Rotas liberadas
    private static final String[] PUBLIC_MATCHERS = {
            "/"
    };

    private static final String[] PUBLIC_MATCHERS_POST = {

            "/funcionario/**", "/chamado/create", "/atendimento/create"

    };

    private static final String[] PUBLIC_MATCHERS_GET = {
            "/funcionario/GetFuncionario",
            "/api/setores/empresa/**",
            "/chamado/**",
            "/Cargo/**",
            "/api/urgencias/empresa/**",
            "/api/status/empresa/**",
            "/urgencia/**",
            "/urgencia/all",
            "/status/**",
            "/status/all",
            "/atendimento/all",
            "/atendimento/listarPorChamado/**",
            "/atendimento/get/**",
            "/atendimento/finalizar/**",
            "/chamado/setorId/nao-concluidos/**",
            "/chamado/atualizarStatus",
            "/meus-chamados"

    };

    // Filtro para permitir todas as rotas e metodos
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        // Permite todas requisições OPTIONS (preflight)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Rotas públicas que você já definiu
                        .requestMatchers(PUBLIC_MATCHERS).permitAll()
                        .requestMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
                        .requestMatchers(PUBLIC_MATCHERS_POST).permitAll()
                        .requestMatchers(HttpMethod.PUT, "/chamado/atualizarStatus").permitAll()

                        .requestMatchers("/**").permitAll()


                        // Qualquer outra requisição precisa estar autenticada
                        .anyRequest().authenticated());

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://127.0.0.1:5500", "http://localhost:5500"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}