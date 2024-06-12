package toy.project.jwt.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import toy.project.jwt.jwt.JwtAuthFilter;
import toy.project.jwt.jwt.JwtUtil;
import toy.project.jwt.service.UserService;


@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    /* 기능 객체 */
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HttpSecurity httpSecurity) throws Exception {
        http
                /* Iframe 랜더링 방지 */
                .headers(headers-> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                /* 기본인증 비활성 */
                .httpBasic().disable()
                /* 크로스 사이트 비활성 */
                .csrf().disable()
                /* 출처가 다른 리소스 접근 비활성 */
                .cors().disable()
                /* 접근 권한 설정 */
                .authorizeRequests()
                    /* 허용하는 사이트 */
                    .requestMatchers("/jwt/signup", "/jwt/login", "/jwt/**", "/api-docs/**").permitAll()
                    /* H2 DB 허용 */
                    .requestMatchers(PathRequest.toH2Console()).permitAll()
                    .and()
                /* 시큐리티 세션 미사용 */
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                /* 필터 설정 */
                .addFilterBefore(new JwtAuthFilter(userService, jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
