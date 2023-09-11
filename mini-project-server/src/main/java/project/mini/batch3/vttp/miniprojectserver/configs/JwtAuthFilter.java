package project.mini.batch3.vttp.miniprojectserver.configs;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter{
    
    private final UserAuthenticationProvider userAuthenticationProvider;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
            String header =  request.getHeader(HttpHeaders.AUTHORIZATION);

            if(header != null) {
                String[] authElements = header.split(" ");

                if (authElements.length == 2 && "Bearer".equals(authElements[0])) {
                    try {
                            SecurityContextHolder.getContext().setAuthentication(
                                userAuthenticationProvider.validateToken(authElements[1])
                            );
                    } catch (JWTVerificationException e) {
                        SecurityContextHolder.clearContext();
                        e.printStackTrace();
                        throw e;
                    }
                }
            }

            filterChain.doFilter(request, response);
        }

}
