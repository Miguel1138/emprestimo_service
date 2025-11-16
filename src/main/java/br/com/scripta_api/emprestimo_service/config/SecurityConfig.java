package br.com.scripta_api.emprestimo_service.config;

//TODO: Anotar a classe com @Configuration, @EnableWebSecurity e @RequiredArgsConstructor.
//
//        TODO: Injetar o jwt.secret (o mesmo do usuario-service): @Value("${jwt.secret}") String jwtSecret;.
//
//TODO: Criar o @Bean principal public SecurityFilterChain securityFilterChain(HttpSecurity http) (conforme RNF-05).
//
//TODO: Dentro do securityFilterChain:
//
//TODO: Desabilitar o CSRF: .csrf(AbstractHttpConfigurer::disable).
//
//TODO: Definir a política de sessão como STATELESS.
//
//        TODO: Configurar o oauth2ResourceServer para usar um jwtDecoder() customizado: .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.decoder(jwtDecoder()).jwtAuthenticationConverter(jwtAuthenticationConverter()))).
//
//TODO: Definir o "Firewall" com authorizeHttpRequests:
//
//        (RF-A05/A07) .requestMatchers(HttpMethod.POST, "/emprestimos", "/emprestimos/**/renovar").hasRole("ALUNO")
//
//(RF-A06) .requestMatchers(HttpMethod.GET, "/emprestimos/meus").hasRole("ALUNO")
//
//(RF-B07) .requestMatchers(HttpMethod.POST, "/emprestimos/devolucao").hasRole("BIBLIOTECARIO")
//
//(RF-B08) .requestMatchers(HttpMethod.GET, "/emprestimos/historico/**").hasRole("BIBLIOTECARIO")
//
//(Padrão) .anyRequest().authenticated()
//
//TODO: Criar o @Bean public JwtDecoder jwtDecoder():
//
//TODO: Criar a SecretKey a partir do jwtSecret (ex: new SecretKeySpec(jwtSecret.getBytes(), "HMACSHA256")).
//
//TODO: Retornar NimbusJwtDecoder.withSecretKey(secretKey).build().
//
//TODO: Criar o @Bean public JwtAuthenticationConverter jwtAuthenticationConverter():
//
//TODO: Criar um JwtGrantedAuthoritiesConverter.
//
//        TODO: Definir o nome da claim de papéis (RNF-07): converter.setAuthoritiesClaimName("roles");
//
//TODO: Remover o prefixo "ROLE_" (pois ele já vem do usuario-service): converter.setAuthorityPrefix("");.
//
//TODO: Criar um JwtAuthenticationConverter, aplicar o converter a ele e retorná-lo.


public class SecurityConfig {

}
