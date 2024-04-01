package KGUcapstone.OutDecision.domain.ranking.service;

import KGUcapstone.OutDecision.domain.ranking.dto.RankingResponseDTO.RankingDTO;
import KGUcapstone.OutDecision.domain.ranking.dto.RankingResponseDTO.RankingListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class RedisRankingServiceImpl implements RankingService {

    private static final String RANKING_KEY = "ranking";
    private final RedisTemplate<String, String> redisTemplate;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RedisRankingServiceImpl(RedisTemplate<String, String> redisTemplate, JdbcTemplate jdbcTemplate) {
        this.redisTemplate = redisTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

//    @Scheduled(fixedRate = 60000)   // 60초 TEST용
    @Scheduled(fixedRate = 3600000) // 1시간(60분)은 3600000밀리초
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

        for (RankingDTO rankingDTO : memberList) {
            Long id = rankingDTO.getMemberId();
            int point = rankingDTO.getPoint();
            String memberId = String.valueOf(id);
            if (existingId.contains(memberId)) {
                // 이미 존재하는 경우, 기존 랭킹 정보를 업데이트(이전 정보 제거)
                zSetOperations.remove(RANKING_KEY + ":point", memberId);
            }
            zSetOperations.add(RANKING_KEY + ":point", memberId, point);
        }
    }

    // 멤버 id로 닉네임 가져오기
    private String getNicknameById(Long id) {
        // MySQL에서 멤버의 닉네임 조회
        return jdbcTemplate.queryForObject("SELECT nickname FROM member WHERE id = ?", String.class, id);
    }

    // 상위 100위권 Ranking
    @Override
    public RankingListDTO getTop100Rankings() {

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
            String nickname = getNicknameById(memberId);

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
            String nickname = getNicknameById(memberId);

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