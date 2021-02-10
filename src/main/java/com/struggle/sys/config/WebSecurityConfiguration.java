package com.struggle.sys.config;

import com.struggle.sys.filter.JsonUsernamePasswordAuthenticationFilter;
import com.struggle.sys.filter.JwtFilter;
import com.struggle.sys.filter.JwtLoginFilter;
import com.struggle.sys.security.*;
import com.struggle.sys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Author WTF
 * @Date 2020/2/11 17:08
 * @Description 全局方法拦截
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    // 自定义过滤器：登录信息将以json的格式接收
    @Bean
    public JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordAuthenticationFilter() throws Exception {
        JsonUsernamePasswordAuthenticationFilter filter = new JsonUsernamePasswordAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

    @Bean
    public JwtLoginFilter jwtLoginFilter() throws Exception {
        return new JwtLoginFilter("/login", authenticationManager());
    }
    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
//    }

    @Autowired
    private CustomAccessDecisionManager customAccessDecisionManager;

    @Autowired
    private CustomFilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Autowired
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @Autowired
    private LoginAuthenticationEntryPoint customAuthenticationEntryPoint;

//    @Autowired
//    private CustomSavedRequestAwareAuthenticationSuccessHandler customSavedRequestAwareAuthenticationSuccessHandler;


    // 登录的用户信息
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("admin").password(passwordEncoder().encode("123456")).roles("ADMIN")
//                .and()
//                .withUser("user").password(passwordEncoder().encode("123456")).roles("USER");
//                .withUser("admin").password("123456").roles("ADMIN")
//                .and()
//                .withUser("user").password("123456").roles("USER");
        auth.userDetailsService(userDetailsService());

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 将 check_token 暴露出去，否则资源服务器访问时报 403 错误
//        web.ignoring().antMatchers("/user1/login");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 登录接口参数Json格式
//        http.addFilterAt(jsonUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests()

//                .antMatchers("/favicon.ico").hasRole("user")
//                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .anyRequest().authenticated()
//                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
//                    @Override
//                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
//                        object.setAccessDecisionManager(customAccessDecisionManager);
//                        object.setSecurityMetadataSource(customFilterInvocationSecurityMetadataSource);
//                        return object;
//                    }
//                })
                .and()
//                .formLogin()
//                .usernameParameter("username")
//                .passwordParameter("password")
//                .loginProcessingUrl("/user/login") // #
//                .loginPage("/user/login") // 默认路径
//                .successHandler(customAuthenticationSuccessHandler)
//                .failureHandler(customAuthenticationFailureHandler)
//                .permitAll()
//                .and()
                .logout()
                .logoutSuccessHandler(customLogoutSuccessHandler)
                .permitAll()
                .and()
                .addFilterBefore(jwtLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable().exceptionHandling()
                //没有认证时，在这里处理结果，不要重定向
                .authenticationEntryPoint(customAuthenticationEntryPoint);

    }


    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("123456"));
    }

}
