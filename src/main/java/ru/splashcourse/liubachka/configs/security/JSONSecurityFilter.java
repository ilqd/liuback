package ru.splashcourse.liubachka.configs.security;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.splashcourse.liubachka.logics.admin.usermanagment.User;

public class JSONSecurityFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * LOGIN_ERROR
     */
    public static final String LOGIN_ERROR = "login_error";

    /**
     * JSONSecurityFilter
     * 
     * @param manager
     *            manager
     * @param modules
     *            active profiles
     */
    public JSONSecurityFilter(AuthenticationManager manager) {
        super(new AntPathRequestMatcher("/login", "POST"));
        setAuthenticationManager(manager);
        setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler() {

            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
                    throws IOException, ServletException {
                clearAuthenticationAttributes(request);
            }
        });
        setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler() {

            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
                    throws IOException, ServletException {
                // super.onAuthenticationFailure(request, response, exception);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                ObjectNode jsonNodes = JsonNodeFactory.instance.objectNode();
                if (exception.getClass().equals(DisabledException.class)) {
                    jsonNodes.put(LOGIN_ERROR, "Учетная запись заблокирована");
                } else {
                    jsonNodes.put(LOGIN_ERROR, exception.getMessage());
                }
                response.getWriter().print(jsonNodes.toString());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        });
    }

    @Getter
    @Setter
    static class LoginInfo {

        private String username;

        private String password;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser parser = new JsonFactory().createParser(request.getReader());
        LoginInfo loginInfo = objectMapper.readValue(parser, LoginInfo.class);
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(loginInfo.getUsername(),
                loginInfo.getPassword());
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        CsrfToken token = (CsrfToken) request.getAttribute("org.springframework.security.web.csrf.CsrfToken");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        response.addIntHeader("AuthSuccessful", 1);
        response.addHeader("CSRF", token.getToken());

        User principal = (User) authResult.getPrincipal();
        response.addHeader("UserId", principal.getId() == null ? "" : principal.getId().toString());
        response.addHeader("Roles", principal.getRoles().isEmpty() ? "" : StringUtils.join(principal.getRoles(), ','));
        Map<String, String> responsePayload = new HashMap<>();
        responsePayload.put("FirstName", principal.getFirstName() == null ? "" : principal.getFirstName());
        responsePayload.put("LastName", principal.getLastName() == null ? "" : principal.getLastName());
        response.getWriter().write(new ObjectMapper().writeValueAsString(responsePayload));
        super.successfulAuthentication(request, response, chain, authResult);
    }

}
