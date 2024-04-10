package KGUcapstone.OutDecision.domain.ranking.service;

import KGUcapstone.OutDecision.domain.ranking.dto.RankingResponseDTO.RankingDTO;
import KGUcapstone.OutDecision.domain.ranking.dto.RankingResponseDTO.RankingListDTO;
import KGUcapstone.OutDecision.domain.title.domain.Title;
import KGUcapstone.OutDecision.domain.title.repository.TitleRepository;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RedisRankingServiceImpl implements RankingService {

    private static final String RANKING_KEY = "ranking";
    private final RedisTemplate<String, String> redisTemplate;
    private final JdbcTemplate jdbcTemplate;
    private final MemberRepository memberRepository;
    private final TitleRepository titleRepository;

//        @Scheduled(cron = "0 6 4 ? * THU") // 테스트용 매주 목요일 04:6분에 실행
    // 매주 월요일 00:00에 포인트 0으로 세팅되도록 스케줄링
    @Scheduled(cron = "0 0 0 * * MON")
    public void resetWeeklyPoints() {
        updateRanking();
        updateTitles();
        // MySQL의 포인트를 0으로 업데이트하는 SQL 쿼리 실행
        String sqlQuery = "UPDATE member SET point = 0";
        jdbcTemplate.update(sqlQuery);
    }

    // 이전 1~3위 칭호 압수 & 새로운 1~3위 칭호 부여
    public void updateTitles() {
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();

        // 랭킹 정보 가져오기
        Set<ZSetOperations.TypedTuple<String>> topRankings = zSetOperations.reverseRangeWithScores(RANKING_KEY + ":point", 0, -1);

        // 이전에 1위였던 회원의 칭호 정보 가져오기
        List<Title> previousFirstTitleList = titleRepository.findByFirstTrue();

        // 이전에 2위였던 회원의 칭호 정보 가져오기
        List<Title> previousSecondTitleList = titleRepository.findBySecondTrue();

        // 이전에 3위였던 회원의 칭호 정보 가져오기
        List<Title> previousThirdTitleList = titleRepository.findByThirdTrue();

        // 이전 1위 회원의 1위 칭호를 false로 변경
        if (!previousFirstTitleList.isEmpty()) {
            for (Title title :previousFirstTitleList) {
                title.setFirst(false);
                titleRepository.save(title);
            }
        }

        // 이전 2위 회원의 2위 칭호를 false로 변경
        if (!previousSecondTitleList.isEmpty()) {
            for (Title title :previousSecondTitleList) {
                title.setSecond(false);
                titleRepository.save(title);
            }
        }

        // 이전 3위 회원의 3위 칭호를 false로 변경
        if (!previousThirdTitleList.isEmpty()) {
            for (Title title :previousThirdTitleList) {
                title.setThird(false);
                titleRepository.save(title);
            }
        }

        // 1위~3위에 해당하는 회원에 대해 칭호 부여
        int rank = 1;
        int prevScore = Integer.MAX_VALUE;
        int sameRankCount = 0;
        for (ZSetOperations.TypedTuple<String> tuple : topRankings) {
            String id = tuple.getValue();
            int point = tuple.getScore().intValue();
            long memberId = Long.parseLong(id);
            Title title = titleRepository.findByMemberId(memberId);

            if (point == 0) break;

            if (point != prevScore) {
                rank += sameRankCount;
                sameRankCount = 1;
            } else {
                sameRankCount++;
            }

            if (rank == 1) title.setFirst(true);
            else if (rank == 2) title.setSecond(true);
            else if (rank == 3) title.setThird(true);
            else break;

            prevScore = point;
            titleRepository.save(title);
        }
    }

    public void updateRanking() {
        // MySQL 데이터베이스에서 데이터 가져오기
        List<RankingDTO> memberList = getDataFromMySQL();

        // Redis에 업데이트된 데이터 저장
        saveOrUpdateRanking(memberList);
    }

    public List<RankingDTO> getDataFromMySQL() {
        // MySQL에서 id와 point 가져와서 List로 저장
        String sqlQuery = "SELECT id, point from member";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) ->
                RankingDTO.builder()
                        .memberId(rs.getLong("id"))
                        .point(rs.getInt("point"))
                        .build());
    }

    private void saveOrUpdateRanking(List<RankingDTO> memberList) {
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        Set<String> existingId = zSetOperations.range(RANKING_KEY + ":point", 0, -1);
        Set<String> mysqlIdSet = memberList.stream()
                .map(rankingDTO -> String.valueOf(rankingDTO.getMemberId()))
                .collect(Collectors.toSet());

        // Redis에는 존재하지만 MySQL에는 없는 회원 정보를 식별하여 제거
        Set<String> redisOnlyIds = new HashSet<>(existingId);
        redisOnlyIds.removeAll(mysqlIdSet);

        for (String redisOnlyId : redisOnlyIds) {
            zSetOperations.remove(RANKING_KEY + ":point", redisOnlyId);
        }

        for (RankingDTO rankingDTO : memberList) {
            Long id = rankingDTO.getMemberId();
            int point = rankingDTO.getPoint();
            String memberId = String.valueOf(id);

            // Redis에는 존재하지 않거나, 존재하더라도 MySQL에 존재하는 회원 정보만 추가
            if (!redisOnlyIds.contains(memberId)) {
                zSetOperations.add(RANKING_KEY + ":point", memberId, point);
            }
        }
    }

    // 상위 100위권 Ranking
    @Override
    public RankingListDTO getTop100Rankings() {

        updateRanking();

        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();

        // 랭킹 정보 가져오기
        Set<ZSetOperations.TypedTuple<String>> topRankings = zSetOperations.reverseRangeWithScores(RANKING_KEY + ":point", 0, -1);

        // 100위까지의 랭킹 정보만 가져와서 반환
        List<RankingDTO> rankingDTOList = new ArrayList<>();
        int rank = 1;
        int prevScore = Integer.MAX_VALUE;
        int sameRankCount = 0;
        for (ZSetOperations.TypedTuple<String> tuple : topRankings) {
            String id = tuple.getValue();
            int point = tuple.getScore().intValue();
            long memberId = Long.parseLong(id);
            String nickname = memberRepository.findNicknameById(memberId);

            if (point != prevScore) {
                rank += sameRankCount;
                sameRankCount = 1;
            } else {
                sameRankCount++;
            }

            if (rank > 100) {
                break; // 100위 이후의 데이터는 처리하지 않음
            }

            RankingDTO rankingDTO = new RankingDTO(rank, memberId, nickname, point);
            rankingDTOList.add(rankingDTO);
            prevScore = point;
        }

        return new RankingListDTO(rankingDTOList, rankingDTOList.size());
    }

    // member의 랭크 조회
    @Override
    public RankingDTO memberRankingDTO(Long memberId) {
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        // 랭킹 정보 가져오기
        Set<ZSetOperations.TypedTuple<String>> topRankings = zSetOperations.reverseRangeWithScores(RANKING_KEY + ":point", 0, -1);

        String reqId = String.valueOf(memberId);

        int rank = 1;
        int prevScore = Integer.MAX_VALUE;
        int sameRankCount = 0;
        RankingDTO rankingDTO = null;
        for (ZSetOperations.TypedTuple<String> tuple : topRankings) {
            String id = tuple.getValue();
            int point = tuple.getScore().intValue();
            String nickname = memberRepository.findNicknameById(memberId);

            if (point != prevScore) {
                rank += sameRankCount;
                sameRankCount = 1;
            } else {
                sameRankCount++;
            }

            if (reqId.equals(id)) {
                rankingDTO = new RankingDTO(rank, memberId, nickname, point);
                break;
            }
        }
        return rankingDTO;
    }
}