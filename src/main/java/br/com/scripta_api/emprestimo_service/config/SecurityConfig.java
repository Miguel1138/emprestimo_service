package br.com.scripta_api.emprestimo_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configura a aplicação como um Resource Server que aceita JWTs
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                )

                .authorizeHttpRequests(authz -> authz
                        // Regras de ALUNO (RF-A05, RF-A06, RF-A07)
                        .requestMatchers(HttpMethod.POST, "/emprestimos").hasRole("ALUNO")
                        .requestMatchers(HttpMethod.POST, "/emprestimos/*/renovar").hasRole("ALUNO")
                        .requestMatchers(HttpMethod.GET, "/emprestimos/meus").hasRole("ALUNO")

                        // Regras de BIBLIOTECARIO (RF-B07, RF-B08)
                        .requestMatchers(HttpMethod.POST, "/emprestimos/devolucao").hasRole("BIBLIOTECARIO")
                        .requestMatchers(HttpMethod.GET, "/emprestimos/historico/**").hasRole("BIBLIOTECARIO")

                        // Qualquer outra requisição exige autenticação
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    /**
     * Bean responsável por decodificar e validar a assinatura do JWT.
     * Usa a chave secreta simétrica (HMAC) compartilhada.
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        var secretKey = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }

    /**
     * Bean responsável por converter as claims do JWT em Autoridades do Spring Security.
     * Configurado para ler a claim "roles" e não adicionar prefixo extra.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles"); // Lê a claim "roles" do token
        grantedAuthoritiesConverter.setAuthorityPrefix(""); // Não adiciona prefixo (o token já tem "ROLE_")

        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtConverter;
    }
}