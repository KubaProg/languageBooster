package eu.pl.main.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.cors.allowed-origins:http://localhost:4200}")
    private List<String> allowedOrigins;

    @Value("${app.jwt.issuer:}")
    private String expectedIssuer;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtDecoder jwtDecoder) throws Exception {
        http
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/public/**", "/actuator/health").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder)));

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(allowedOrigins);
        cfg.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
        cfg.setAllowCredentials(false); // Bearer tokens do not need credentials
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    /**
     * === Hosted Supabase (RS256) path ===
     * If issuer-uri is present, prefer remote JWKS validation.
     * Configure in properties:
     *   spring.security.oauth2.resourceserver.jwt.issuer-uri=https://<PROJECT_REF>.supabase.co/auth/v1
     *   (optionally) spring.security.oauth2.resourceserver.jwt.jwk-set-uri=https://<PROJECT_REF>.supabase.co/auth/v1/keys
     */
    @Bean
    @ConditionalOnProperty(prefix = "spring.security.oauth2.resourceserver.jwt", name = "issuer-uri")
    JwtDecoder jwksDecoder(
            @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuer
    ) {
        NimbusJwtDecoder decoder = JwtDecoders.fromIssuerLocation(issuer);
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(withIssuer));
        return decoder;
    }

    /**
     * === Local Supabase (HS256) path ===
     * Active when issuer-uri is NOT set and app.jwt.hmac-secret IS set.
     * Configure in properties:
     *   app.jwt.hmac-secret=<YOUR_LOCAL_SUPABASE_JWT_SECRET>
     *   app.jwt.issuer=http://127.0.0.1:54321/auth/v1
     */
    @Bean
    @ConditionalOnProperty(prefix = "app.jwt", name = "hmac-secret")
    JwtDecoder hmacDecoder(
            @Value("${app.jwt.hmac-secret}") String hmacSecret
    ) {
        SecretKeySpec key = new SecretKeySpec(hmacSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();

        OAuth2TokenValidator<Jwt> defaultValidators = JwtValidators.createDefault();
        OAuth2TokenValidator<Jwt> issuerValidator = (jwt) -> {
            if (expectedIssuer == null || expectedIssuer.isBlank()) return OAuth2TokenValidatorResult.success();
            String iss = jwt.getIssuer() != null ? jwt.getIssuer().toString() : "";
            return iss.equals(expectedIssuer)
                    ? OAuth2TokenValidatorResult.success()
                    : OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "Invalid issuer", null));
        };
        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(defaultValidators, issuerValidator));
        return decoder;
    }
}
