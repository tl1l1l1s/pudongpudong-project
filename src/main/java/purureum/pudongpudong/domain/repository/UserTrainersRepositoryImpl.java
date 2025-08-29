package purureum.pudongpudong.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import purureum.pudongpudong.domain.model.UserTrainers;

import java.util.List;
import java.util.Optional;

import static purureum.pudongpudong.domain.model.QUserTrainers.userTrainers;
import static purureum.pudongpudong.domain.model.QTrainers.trainers;
import static purureum.pudongpudong.domain.model.QParks.parks;

@RequiredArgsConstructor
public class UserTrainersRepositoryImpl implements UserTrainersRepositoryCustom {
	
	private final JPAQueryFactory queryFactory;
	
	@Override
	public List<UserTrainers> findByUserId(Long userId) {
		return queryFactory
				.selectFrom(userTrainers)
				.join(userTrainers.trainer, trainers).fetchJoin()
				.join(trainers.park, parks).fetchJoin()
				.where(userTrainers.id.userId.eq(userId))
				.fetch();
	}
	
	@Override
	public Integer countByUserId(Long userId) {
		Long count = queryFactory
				.select(userTrainers.count())
				.from(userTrainers)
				.where(userTrainers.id.userId.eq(userId))
				.fetchOne();
		return count != null ? count.intValue() : 0;
	}
	
	@Override
	public Optional<UserTrainers> findByUserIdAndTrainerId(Long userId, Long trainerId) {
		UserTrainers result = queryFactory
				.selectFrom(userTrainers)
				.join(userTrainers.trainer, trainers).fetchJoin()
				.leftJoin(trainers.park, parks).fetchJoin()  // leftJoin으로 변경
				.where(userTrainers.id.userId.eq(userId)
						.and(userTrainers.id.trainerId.eq(trainerId)))
				.fetchOne();
		return Optional.ofNullable(result);
	}
}