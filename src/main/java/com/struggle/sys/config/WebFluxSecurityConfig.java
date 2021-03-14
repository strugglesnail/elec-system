package com.struggle.sys.config;

//import com.struggle.flux.filter.JwtFilter;
import com.struggle.sys.filter.JwtAuthorizationFilter;
import com.struggle.sys.filter.JwtFilter;
import com.struggle.sys.filter.JwtLoginFilter;
import com.struggle.sys.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationWebFilter;

/**
 * @author strugglesnail
 * @date 2021/3/2
 * @desc
 */
@Configuration
@EnableWebFluxSecurity
public class WebFluxSecurityConfig {

    @Autowired
    private JsonServerAuthenticationSuccessHandler jsonServerAuthenticationSuccessHandler;
    @Autowired
    private JsonAuthenticationFailureHandler jsonServerAuthenticationFailureHandler;

    @Autowired
    private JsonLogoutSuccessHandler jsonLogoutSuccessHandler;
    @Autowired
    private LoginAuthenticationEntryPoint loginAuthenticationEntryPoint;

    private static final String[] AUTH_WHITELIST = new String[]{"/login", "/logout", "/user/info"};

    @Autowired
    private AuthenticationManager reactiveAuthenticationManager;
    @Autowired
    private AuthorizationManager reactiveAuthorizationManager;

    @Bean
    public JwtLoginFilter jwtLoginFilter() {
        return new JwtLoginFilter("/login", reactiveAuthenticationManager);
    }
//    @Bean
//    public JwtAuthorizationFilter jwtAuthorizationFilter() {
//        return new JwtAuthorizationFilter(reactiveAuthorizationManager);
//    }
    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter();
    }


    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//        return http
//                .formLogin()
//                .loginPage("/login")
//                .and()
//                .logout().logoutSuccessHandler(jsonLogoutSuccessHandler)
//                .and()
//                .csrf().disable()
//                .authorizeExchange()
//                .pathMatchers("/login", "/flux/1").permitAll()
//                .anyExchange().authenticated()
//                .and()
////                .addFilterAt(jwtLoginFilter(), SecurityWebFiltersOrder.FIRST) // 这里注意执行位置一定要在securityContextRepository
//                .exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint)
//                .and()
//                .build();

        SecurityWebFilterChain chain = http
                .formLogin().loginPage("/login")
                // 登录成功handler
                .authenticationSuccessHandler(jsonServerAuthenticationSuccessHandler)
                // 登陆失败handler
                .authenticationFailureHandler(jsonServerAuthenticationFailureHandler)
                .and()
                // 登出成功handler
                .logout()
                .logoutSuccessHandler(jsonLogoutSuccessHandler)
                .and()
                .csrf().disable()
                .httpBasic().disable()
                .authorizeExchange()
                // 白名单放行
                .pathMatchers(AUTH_WHITELIST).permitAll()
                // 访问权限控制
                .anyExchange().access(reactiveAuthorizationManager)
                .and()
                .addFilterAt(jwtLoginFilter(), SecurityWebFiltersOrder.FORM_LOGIN)

                // 无访问权限handler
                .exceptionHandling().authenticationEntryPoint(loginAuthenticationEntryPoint)

                .and().build();
//        // 设置自定义登录参数转换器
//        chain.getWebFilters()
//                .filter(webFilter -> webFilter instanceof AuthorizationWebFilter)
//                .map(webFilter -> jwtAuthorizationFilter());
        return chain;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //默认加密
    }

}


