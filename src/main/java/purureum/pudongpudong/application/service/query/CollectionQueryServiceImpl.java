package purureum.pudongpudong.application.service.query;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import purureum.pudongpudong.domain.model.Parks;
import purureum.pudongpudong.domain.model.Species;
import purureum.pudongpudong.domain.model.Trainers;
import purureum.pudongpudong.domain.model.UserTrainers;
import purureum.pudongpudong.domain.repository.ParkSpeciesRepository;
import purureum.pudongpudong.domain.repository.TrainersRepository;
import purureum.pudongpudong.domain.repository.UserStampsRepository;
import purureum.pudongpudong.domain.repository.UserTrainersRepository;
import purureum.pudongpudong.global.apiPayload.code.status.ErrorStatus;
import purureum.pudongpudong.global.apiPayload.exception.handler.ApiHandler;
import purureum.pudongpudong.global.util.MathUtil;
import purureum.pudongpudong.infrastructure.dto.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CollectionQueryServiceImpl implements CollectionQueryService{

	private final UserTrainersRepository userTrainersRepository;
	private final UserStampsRepository userStampsRepository;
	private final TrainersRepository trainersRepository;
	private final ParkSpeciesRepository parkSpeciesRepository;

	@Override
	public CollectionResponseDto getCollectionStatus(Long userId) {
		CollectionOverviewDto overview = buildOverview(userId);
		List<CollectionTrainerDto> trainers = buildTrainerCollection(userId);

		log.info("컬렉션 현황 조회: userId={}, unlockedTrainers={}, collectedStamps={}",
				userId, overview.getUnlockedTrainers(), overview.getCollectedStamps());

		return CollectionResponseDto.builder()
				.overview(overview)
				.trainers(trainers)
				.build();
	}

	@Override
	public TrainerDetailResponseDto getTrainerDetail(Long userId, Long trainerId) {
		Trainers trainer = findTrainerById(trainerId);
		UserTrainers userTrainer = findUserTrainerById(userId, trainerId);

		if (trainer.getPark() == null) {
			throw new ApiHandler(ErrorStatus.TRAINER_PARK_NOT_ASSIGNED);
		}

		String trainerName = trainer.getName();
		String parkId = trainer.getPark().getId();
		List<Species> trainerSpecies = findSpeciesByParkId(parkId);

		if (trainerSpecies.isEmpty()) {
			throw new ApiHandler(ErrorStatus.NO_SPECIES_IN_PARK);
		}

		List<Species> userCollectedSpecies = findUserCollectedSpeciesByUserIdAndParkId(userId, parkId);

		List<TrainerStampDto> stamps = buildTrainerStamps(trainerSpecies);
		
		log.info("트레이너 상세 조회: userId={}, trainerId={}, trainerName={}, collectedStamps={}, totalStamps={}",
				userId, trainerId, trainerName, userCollectedSpecies.size(), trainerSpecies.size());

		return TrainerDetailResponseDto.builder()
				.trainerName(trainerName)
				.description(trainer.getDescription())
				.comment("")
				.stamps(stamps)
				.collectedStamps(userCollectedSpecies.size())
				.totalStamps(trainerSpecies.size())
				.build();
	}

	private CollectionOverviewDto buildOverview(Long userId) {
		Integer unlockedTrainers = userTrainersRepository.countByUserId(userId);
		long totalTrainers = trainersRepository.count();
		Integer lockedTrainers = (int) totalTrainers - unlockedTrainers;

		Integer collectedStamps = userStampsRepository.countDistinctSpeciesByUserId(userId);
		Integer totalStamps = userStampsRepository.countTotalSpecies();

		double completionPercentage = MathUtil.calculateCompletionPercentage(collectedStamps, totalStamps);

		return CollectionOverviewDto.builder()
				.unlockedTrainers(unlockedTrainers)
				.lockedTrainers(lockedTrainers)
				.collectedStamps(collectedStamps)
				.completionPercentage(completionPercentage)
				.build();
	}

	private List<CollectionTrainerDto> buildTrainerCollection(Long userId) {
		List<UserTrainers> userTrainers = userTrainersRepository.findByUserId(userId);

		if (userTrainers.isEmpty()) {
			return new ArrayList<>();
		}

		Map<String, Integer> stampsMap = userStampsRepository.countSpeciesByUserIdAndPark(userId);
		List<CollectionTrainerDto> result = new ArrayList<>();

		for (UserTrainers userTrainer : userTrainers) {
			Parks park = userTrainer.getTrainer().getPark();
			if (park == null) {
				log.warn("트레이너에 공원이 할당되지 않았습니다: trainerId={}", userTrainer.getTrainer().getId());
				continue; 
			}

			String parkName = park.getPlaceName();
			Integer totalSpecies = parkSpeciesRepository.countByParkId(park.getId());
			Integer collectedSpecies = stampsMap.getOrDefault(parkName, 0);

			CollectionTrainerDto dto = CollectionTrainerDto.builder()
					.parkName(parkName)
					.trainerName(userTrainer.getTrainer().getName())
					.totalSpecies(totalSpecies)
					.collectedSpecies(collectedSpecies)
					.build();

			result.add(dto);
		}

		return result;
	}

	private Trainers findTrainerById(Long trainerId) {
		return trainersRepository.findById(trainerId)
				.orElseThrow(() -> new ApiHandler(ErrorStatus.TRAINER_NOT_FOUND));
	}

	private UserTrainers findUserTrainerById(Long userId, Long trainerId) {
		return userTrainersRepository.findByUserIdAndTrainerId(userId, trainerId)
				.orElseThrow(() -> new ApiHandler(ErrorStatus.USER_TRAINER_NOT_FOUND));
	}

	private List<Species> findSpeciesByParkId(String parkId) {
		return parkSpeciesRepository.findSpeciesByParkId(parkId);
	}

	private List<Species> findUserCollectedSpeciesByUserIdAndParkId(Long userId, String parkId) {
		return userStampsRepository.findCollectedSpeciesByUserIdAndParkId(userId, parkId);
	}

	private List<TrainerStampDto> buildTrainerStamps(List<Species> species) {
		List<TrainerStampDto> stamps = new ArrayList<>();
		for (Species s : species) {
			stamps.add(TrainerStampDto.builder()
					.speciesName(s.getName())
					.build());
		}
		return stamps;
	}
}
