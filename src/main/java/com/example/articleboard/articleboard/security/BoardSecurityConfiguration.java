package com.example.articleboard.articleboard.security;

import com.example.articleboard.articleboard.security.jwt.JwtConfigure;
import com.example.articleboard.articleboard.security.jwt.JwtProvider;
import org.modelmapper.ModelMapper;
import com.example.articleboard.articleboard.security.auth.AuthDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
// 필터체인 bean으로 등록하고 WebSecurityAdaptor 빼버리기
public class BoardSecurityConfiguration {

    private final JwtProvider jwtProvider;
    private final AuthDetailsService authDetailsService;
    private final Environment environment;


    CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowedOriginPatterns(Collections.singletonList("http://localhost:3000")); // ⭐️ 허용할 origin
            config.setAllowCredentials(true);
            return config;
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return authDetailsService;
    }
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().disable()
                /*.cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource())) // ⭐️⭐️⭐️
                .csrf(AbstractHttpConfigurer::disable)*/
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                /*.headers()
                .cacheControl()
                .disable()
                .and()*/
                .authorizeRequests()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/dealers/**").permitAll()
                .antMatchers("/gallery/**").permitAll()
                .antMatchers("/media/**").permitAll()
                .antMatchers("/newsletter/**").permitAll()
                .antMatchers("/image/**").permitAll()
                .antMatchers("/video/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/user/login").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/user/signUp").permitAll()
                .antMatchers("/user/checkUsername").permitAll()
                .antMatchers("/admin/**").permitAll()
                .antMatchers("/contact/**").permitAll()
                .antMatchers("/membership/DealerAgent/insert").authenticated()
                .antMatchers("/membership/DealerAgent/edit/**").authenticated()
                .antMatchers("/membership/**").permitAll()
                .antMatchers("/assets/favicon.ico").permitAll()
                .antMatchers("/auth/refreshToken").permitAll()
                .antMatchers("/api/post/list/**").permitAll()
                .antMatchers("/api/post/search/**").permitAll()
                .antMatchers("/api/post/get/**").permitAll()
                .antMatchers("/api/admin").permitAll()
                .antMatchers("/api/admin/search/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/templates/th/turbo/company/**").permitAll()
                .antMatchers("/company/**").permitAll()
                .antMatchers("/coretech/**").permitAll()
                .antMatchers("/product/**").permitAll()
                .antMatchers("/energysave/**").permitAll()
                .antMatchers("/html/**").permitAll()
                .antMatchers("/eng/**").permitAll()
                /*.antMatchers("/post/**").permitAll()*/

                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigure(jwtProvider, environment));

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers(
                "/assets/favicon.ico",
                "/",
                "/smartEditor/**",
                "/dealers/**",
                "/newsletter/**",
                "/video/**",
                "/image/**",
                "/gallery/**",
                "/media/**",
                "/webjars/**",
                "/js/**",
                "/images/**",
                "/user/login",
                "/user/signUp",
                "/user/checkUsername",
                "/css/**",
                "/post/**",
                "/contact/**",
                "/membership/**",
                "/admin/**",
                "/api/post/get/**",
                "/api/post/list/**",
                "/api/post/search/**",
                "/api/admin/search/**",
                "/api/admin/**",
                "/company/**",
                "/coretech/**",
                "/product/**",
                "/energysave/**",
                "/templates/th/turbo/company/**",
                "/html/**",
                "/eng/**",
                "/auth/refreshToken"
        );
    }
}
