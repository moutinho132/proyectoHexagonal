package com.martzatech.vdhg.crmprojectback.infrastructure.configs;

import com.azure.spring.cloud.autoconfigure.implementation.aad.security.AadResourceServerHttpSecurityConfigurer;
import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.domains.security.models.Permission;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.domains.security.services.PermissionService;
import com.martzatech.vdhg.crmprojectback.domains.security.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableMethodSecurity
@Profile(value = {"local", "development", "stage", "staging"})
@Slf4j
public class CustomOAuth2ResourceServerSecurityConfig {

    private static final String API_PREFIX = "/api";
    private static final String WILDCARD_ALL = "/**";
    private static final String ROLE_PREFIX = "ROLE_";

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserService userService;

    @Bean
    @Order(1)
    public SecurityFilterChain authorizeApiRequests(final HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors().and()
                .securityMatcher(API_PREFIX + WILDCARD_ALL)
                .authorizeHttpRequests(
                        authorize -> {
                            authorize
                                    .requestMatchers(HttpMethod.GET, API_PREFIX + "/notes/**")
                                    .authenticated();

                            authorize
                                    .requestMatchers(HttpMethod.GET, API_PREFIX + "/security/users/current")
                                    .authenticated();

                            authorize
                                    .requestMatchers(HttpMethod.GET, API_PREFIX + "/v2/**")
                                    .permitAll();

                            authorize
                                    .requestMatchers(HttpMethod.POST, API_PREFIX + "/v2/**")
                                    .permitAll();


                            authorize
                                    .requestMatchers(HttpMethod.POST, API_PREFIX + "/v2/security/login")
                                    .permitAll();
                        }
                )
                .authorizeHttpRequests(customAuthorizations())
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(source -> new CustomJwtConfigure().convert(source));
        return http.build();
    }

    private Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>
            .AuthorizationManagerRequestMatcherRegistry> customAuthorizations() {
        return authorize -> {
            try {
                permissionService.findAll()
                        .stream()
                        .collect(
                                Collectors
                                        .groupingBy(
                                                Permission::getMethod, Collectors.groupingBy(Permission::getResource)
                                        )
                        )
                        .forEach((method, resources) -> {
                            resources.forEach((resource, p) -> {
                                final String[] roles = p.stream()
                                        .filter(permission -> StringUtils.isEmpty(permission.getField()))
                                        .map(Permission::getName)
                                        .toArray(String[]::new);
                                log.info("Configured ROLEs {} in {} : {}", roles, method, resource);
                                authorize
                                        .requestMatchers(
                                                HttpMethod.valueOf(method),
                                                API_PREFIX + resource)
                                        .hasAnyRole(roles);
                            });
                        });

                authorize
                        .anyRequest()
                        .authenticated();
            } catch (final Exception e) {
                log.error("There was an error in authorizeHttpRequests", e);
            }
        };
    }

    @Bean
    @Order(2)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .apply(AadResourceServerHttpSecurityConfigurer.aadResourceServer())
                .and()
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers(
                                        "/v3/api-docs/**",
                                        "/blob/write/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/api/v2/security/users/app")

                                .permitAll()
                )
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());
        return http.build();
    }

    public class CustomJwtConfigure implements Converter<Jwt, JwtAuthenticationToken> {

        @Override
        public JwtAuthenticationToken convert(final Jwt jwt) {
            final Map<String, Object> tokenAttributes = jwt.getClaims();
            final String email = (String) tokenAttributes.get(CommonConstants.EMAIL_MS_KEY);
            return new JwtAuthenticationToken(jwt, mappedAuthorities(userService.findByEmail(email)));
        }
    }

    private List<? extends GrantedAuthority> mappedAuthorities(final User user) {
        return user.getRoles()
                .stream()
                .flatMap(role -> role.getPermissions().stream())
                .filter(permission -> StringUtils.isEmpty(permission.getField()))
                .map(permission -> new SimpleGrantedAuthority(ROLE_PREFIX + permission.getName().toUpperCase(Locale.ROOT)))
                .toList();
    }
}
