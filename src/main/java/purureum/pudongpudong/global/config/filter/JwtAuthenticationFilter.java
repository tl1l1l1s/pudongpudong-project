package purureum.pudongpudong.global.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import purureum.pudongpudong.domain.repository.UsersRepository;
import purureum.pudongpudong.global.util.JwtUtil;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UsersRepository usersRepository;
	
	private static final String[] WHITELIST_URLS = {
			"/api/auth/**",
			"/api/health/**",
			"/swagger-ui/**",
			"/v3/api-docs/**",
			"/swagger-resources/**"
	};
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String path = request.getRequestURI();
		for (String url : WHITELIST_URLS) {
			if (path.startsWith(url)) {
				return true;
			}
		}
		return false;
	}
	
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ") && jwtUtil.validateToken(bearerToken.substring(7))) {
            String token = bearerToken.substring(7);
            Long userId = jwtUtil.getUserIdFromToken(token);
            usersRepository.findById(userId).ifPresent(user -> {
                UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getProviderId(), "", new ArrayList<>());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("인증 성공: userId={}", userId);
            });
        }

        filterChain.doFilter(request, response);
    }
}
