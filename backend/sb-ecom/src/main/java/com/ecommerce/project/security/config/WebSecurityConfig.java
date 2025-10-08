package com.ecommerce.project.security.config;

import com.ecommerce.project.model.AppRole;
import com.ecommerce.project.model.Roles;
import com.ecommerce.project.model.User;
import com.ecommerce.project.repository.RoleRepository;
import com.ecommerce.project.repository.UserRepository;
import com.ecommerce.project.security.jwt.AuthTokenFilter;
import com.ecommerce.project.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Set;

@Configuration
@EnableWebSecurity
// @EnableMethodSecurity
public class WebSecurityConfig {
    @Autowired
    private AuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Bean
    AuthTokenFilter authJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
            throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);
        http.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler));
        http.sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        http.authorizeHttpRequests(
                authorizeRequest ->
                        authorizeRequest
                                .requestMatchers("/api/auth/**")
                                .permitAll()
                                .requestMatchers("/v3/api-docs/**")
                                .permitAll()
                                .requestMatchers("/swagger-ui/**")
                                .permitAll()
//                .requestMatchers("/api/public/**")
//                .permitAll()
//                .requestMatchers("/api/admin/**")
//                .permitAll()
                                .requestMatchers("/api/test/**")
                                .permitAll()
                                .requestMatchers("/images/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated());
        http.authenticationProvider(daoAuthenticationProvider());
        http.addFilterBefore(authJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web ->
                web.ignoring()
                        .requestMatchers(
                                "/v2/api-docs",
                                "/configuration/ui",
                                "/swagger-resources/**",
                                "/configuration/security",
                                "/swagger-ui.html",
                                "/webjars/**"));
    }

    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            Roles userRole = roleRepository.findByRoleName(AppRole.ROLE_USER).orElseGet(() -> {
                Roles newUserRole = new Roles(AppRole.ROLE_USER);
                return roleRepository.save(newUserRole);
            });
            Roles sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER).orElseGet(() -> {
                Roles newSellerRole = new Roles(AppRole.ROLE_SELLER);
                return roleRepository.save(newSellerRole);
            });

            Roles adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN).orElseGet(() -> {
                Roles newAdminRole = new Roles(AppRole.ROLE_ADMIN);
                return roleRepository.save(newAdminRole);
            });
            Set<Roles> userRoles = Set.of(userRole);
            Set<Roles> sellerRoles = Set.of(sellerRole);
            Set<Roles> adminRoles = Set.of(adminRole, userRole, sellerRole);


            if (!userRepository.existsByUsername("user1")) {
                User user = new User("user1", "user@email.com", encoder.encode("user@123"));
                userRepository.save(user);
            }
            if (!userRepository.existsByUsername("seller")) {
                User seller = new User("seller", "seller@email.com", encoder.encode("seller@123"));
                userRepository.save(seller);
            }
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User("admin", "admin@email.com", encoder.encode("admin@123"));
                userRepository.save(admin);
            }

            userRepository.findByUsername("user1").ifPresent(user -> {
                user.setRoles(userRoles);
                userRepository.save(user);
            });
            userRepository.findByUsername("seller1").ifPresent(seller -> {
                seller.setRoles(sellerRoles);
                userRepository.save(seller);
            });
            userRepository.findByUsername("admin").ifPresent(admin -> {
                admin.setRoles(adminRoles);
                userRepository.save(admin);
            });
        };
    }
}
