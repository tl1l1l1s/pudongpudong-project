package purureum.pudongpudong.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import purureum.pudongpudong.domain.model.JwtDenylist;
import purureum.pudongpudong.domain.model.Users;
import purureum.pudongpudong.domain.repository.JwtDenylistRepository;
import purureum.pudongpudong.domain.repository.UsersRepository;
import purureum.pudongpudong.global.util.JwtUtil;
import purureum.pudongpudong.infrastructure.dto.AuthRequestDto;
import purureum.pudongpudong.infrastructure.dto.AuthResponseDto;
import purureum.pudongpudong.global.apiPayload.code.status.ErrorStatus;
import purureum.pudongpudong.global.apiPayload.exception.handler.ApiHandler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
	
	private final UsersRepository usersRepository;
	private final JwtDenylistRepository jwtDenylistRepository;
	private final JwtUtil jwtUtil;
	
	@Transactional
	public AuthResponseDto signUp(AuthRequestDto request) {
		if (request.getProviderId() == null || request.getProviderId().trim().isEmpty()) {
			throw new ApiHandler(ErrorStatus.INVALID_AUTH_DATA);
		}
		if (request.getName() == null || request.getName().trim().isEmpty()) {
			throw new ApiHandler(ErrorStatus.INVALID_AUTH_DATA);
		}
		if (request.getProvider() == null) {
			throw new ApiHandler(ErrorStatus.INVALID_AUTH_DATA);
		}
		
		Users user = usersRepository.findByProviderId(request.getProviderId())
				.orElse(null);
		
		boolean isNewUser = false;
		
		if (user == null) {
			user = Users.builder()
					.name(request.getName())
					.providerId(request.getProviderId())
					.provider(request.getProvider())
					.build();
			user = usersRepository.save(user);
			isNewUser = true;
			log.info("신규 사용자 생성: providerId={}, provider={}", request.getProviderId(), request.getProvider());
		} else {
			log.info("기존 사용자 로그인: providerId={}, provider={}", request.getProviderId(), request.getProvider());
		}
		
		String token = generateJwtToken(user.getId());
		
		return AuthResponseDto.builder()
				.userId(user.getId())
				.name(user.getName())
				.providerId(user.getProviderId())
				.provider(user.getProvider())
				.token(token)
				.isNewUser(isNewUser)
				.build();
	}
	
	private String generateJwtToken(Long userId) {
		try {
			return jwtUtil.generateToken(userId);
		} catch (Exception e) {
			log.error("JWT 토큰 생성 실패: userId={}", userId, e);
			throw new ApiHandler(ErrorStatus.TOKEN_GENERATION_FAILED);
		}
	}

	@Transactional
	public void logout(String bearerToken) {
		if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
			throw new ApiHandler(ErrorStatus.INVALID_TOKEN_FORMAT);
		}
		String token = bearerToken.substring(7);

		String signature = jwtUtil.getSignatureFromToken(token);
		Date expirationDate = jwtUtil.getExpirationDateFromToken(token);
		LocalDateTime expiresAt = expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

		JwtDenylist denylistedToken = JwtDenylist.builder()
				.signature(signature)
				.expiresAt(expiresAt)
				.build();

		jwtDenylistRepository.save(denylistedToken);
		log.info("토큰 무효화 완료: signature={}", signature);
	}
}