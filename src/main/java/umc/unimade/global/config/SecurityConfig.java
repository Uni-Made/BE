package umc.unimade.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.web.client.RestTemplate;
import umc.unimade.global.security.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailService buyerUserDetailService;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtProvider jwtProvider;
    private final JwtEntryPoint jwtEntryPoint;

    private final String[] swaggerUrls = {"/swagger-ui/**", "/v3/**"};

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> {authorize
                        .requestMatchers(HttpMethod.GET, "/admin/notice", "/admin/notice/{noticeBoardId}").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/products/favorite/**", "/api/buyer/**").hasRole("BUYER")
                        .requestMatchers("/seller/**").hasRole("SELLER")
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(swaggerUrls).permitAll()
                        .anyRequest().authenticated();})
                .oauth2Login(oauth2 -> oauth2 // OAuth2 로그인 설정
                        .loginPage("/oauth2/authorization/messaging-client-oidc")
                        .defaultSuccessUrl("/loginSuccess")
                        .failureUrl("/loginFailure"))
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(jwtEntryPoint)
                                .accessDeniedHandler(jwtAccessDeniedHandler))
                .headers(headers -> headers.xssProtection(
                                xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)
                        ).contentSecurityPolicy(
                                cps -> cps.policyDirectives("script-src 'self'")
                        )
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider, buyerUserDetailService),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(), JwtAuthenticationFilter.class)
                .addFilterAfter(new AdminAccessFilter(), JwtAuthenticationFilter.class);
        return http.build();
    }

}
