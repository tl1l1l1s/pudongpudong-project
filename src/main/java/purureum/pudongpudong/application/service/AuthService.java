package purureum.pudongpudong.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import purureum.pudongpudong.domain.model.Users;
import purureum.pudongpudong.domain.repository.UsersRepository;
import purureum.pudongpudong.global.util.JwtUtil;
import purureum.pudongpudong.infrastructure.dto.AuthRequestDto;
import purureum.pudongpudong.infrastructure.dto.AuthResponseDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
	
	private final UsersRepository usersRepository;
	private final JwtUtil jwtUtil;
	
	@Transactional
	public AuthResponseDto signUp(AuthRequestDto request) {
		// 기존 사용자 확인
		Users user = usersRepository.findByProviderId(request.getProviderId())
				.orElse(null);
		
		boolean isNewUser = false;
		
		if (user == null) {
			// 신규 사용자 생성
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
		
		// JWT 토큰 생성
		String token = jwtUtil.generateToken(user.getId());
		
		return AuthResponseDto.builder()
				.userId(user.getId())
				.name(user.getName())
				.providerId(user.getProviderId())
				.provider(user.getProvider())
				.token(token)
				.isNewUser(isNewUser)
				.build();
	}
}