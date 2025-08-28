package purureum.pudongpudong.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import purureum.pudongpudong.domain.model.UserTrainers;

import java.util.List;

import static purureum.pudongpudong.domain.model.QUserTrainers.userTrainers;
import static purureum.pudongpudong.domain.model.QTrainers.trainers;

@RequiredArgsConstructor
public class UserTrainersRepositoryImpl implements UserTrainersRepositoryCustom {
	
	private final JPAQueryFactory queryFactory;
	
	@Override
	public List<UserTrainers> findByUserId(Long userId) {
		return queryFactory
				.selectFrom(userTrainers)
				.join(userTrainers.trainer, trainers).fetchJoin()
				.where(userTrainers.user.id.eq(userId))
				.fetch();
	}
	
	@Override
	public Integer countByUserId(Long userId) {
		Long count = queryFactory
				.select(userTrainers.count())
				.from(userTrainers)
				.where(userTrainers.user.id.eq(userId))
				.fetchOne();
		return count != null ? count.intValue() : 0;
	}
}