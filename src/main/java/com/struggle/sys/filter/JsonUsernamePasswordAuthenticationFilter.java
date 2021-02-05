package com.struggle.sys.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @auther strugglesnail
 * @date 2021/1/24 17:17
 * @desc
 */
public class JsonUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authToken = null;
        // Json格式形式
        if (request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)) {
            ObjectMapper mapper = new ObjectMapper();
            try(InputStream is = request.getInputStream()) {
                Map<String, String> userMap = mapper.readValue(is, Map.class);
                authToken = new UsernamePasswordAuthenticationToken(userMap.get("username"), userMap.get("password"));
            } catch (IOException e) {
                e.printStackTrace();
                authToken = new UsernamePasswordAuthenticationToken("", "");
            } finally {
                setDetails(request, authToken);
                return this.getAuthenticationManager().authenticate(authToken);
            }
        } else {
            // Form表单形式
            return super.attemptAuthentication(request, response);
        }

    }
}
