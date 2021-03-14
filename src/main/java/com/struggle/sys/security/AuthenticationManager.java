package com.struggle.sys.security;

import com.struggle.sys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * @author strugglesnail
 * @date 2021/3/2
 * @desc
 */
@Component
public class AuthenticationManager extends AbstractUserDetailsReactiveAuthenticationManager {

    private Scheduler scheduler = Schedulers.boundedElastic();

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserService userService;


    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        final String username = authentication.getName();
        final String presentedPassword = (String) authentication.getCredentials();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(presentedPassword)) {
            return Mono.error(new AuthenticationCredentialsNotFoundException("请输入账号或密码"));
        }
        return retrieveUser(username)
                .publishOn(scheduler)
                .filter(u -> passwordEncoder.matches(presentedPassword, u.getPassword()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BadCredentialsException("Invalid Credentials"))))
                .flatMap(u -> {
                    boolean upgradeEncoding = userService != null
                            && passwordEncoder.upgradeEncoding(u.getPassword());
                    if (upgradeEncoding) {
                        String newPassword = passwordEncoder.encode(presentedPassword);
                        return userService.updatePassword(u, newPassword);
                    }
                    return Mono.just(u);
                })
                .flatMap(userDetails -> {
                    // 省略业务代码
                    return Mono.just(userDetails);
                })
                .map(u -> new UsernamePasswordAuthenticationToken(u, u.getPassword(), u.getAuthorities()));
    }

    @Override
    protected Mono<UserDetails> retrieveUser(String account) {
        return userService.findByUsername(account);
    }

}
