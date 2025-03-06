package playCom.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@Component
public class AuthFilter extends OncePerRequestFilter {

    private final WebClient webClient = WebClient.create("http://authentification-api:8080");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {


        String token = request.getHeader("Authorization");

        if (token == null || token.isEmpty() || !validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Unauthorized: Invalid or expired token\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean validateToken(String token) {
        String response = webClient.post()
                .uri("/auth/validate")
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return response != null && !response.contains("Token expiré") && !response.contains("Token invalide");
    }
}
