package KGUcapstone.OutDecision.domain.post.service;

import KGUcapstone.OutDecision.domain.post.domain.enums.Category;
import KGUcapstone.OutDecision.domain.user.domain.MemberView;

import java.util.*;

public class UserBasedCF {

    private final int topSimilarMembers;        // 상위 유사 사용자의 수
    private final Map<Long, Map<Category, Integer>> memberDataMap;  // 사용자 데이터를 맵으로 변환하여 저장
    private final Map<Long, Map<Long, Double>> similarityMatrix;  // 유사도 행렬 저장

    public UserBasedCF(List<MemberView> memberData, int topSimilarMembers) {
        this.topSimilarMembers = topSimilarMembers;
        this.memberDataMap = convertMemberDataToMap(memberData);
        this.similarityMatrix = computeSimilarityMatrix();    //유사도 행렬 계산
    }

    private Map<Long, Map<Category, Integer>> convertMemberDataToMap(List<MemberView> memberData) {
        Map<Long, Map<Category, Integer>> memberDataMap = new HashMap<>();
        for (MemberView memberView : memberData) {
            long memberId = memberView.getMember().getId();
            Category category = memberView.getCategory();
            int views = memberView.getViews();
            memberDataMap.computeIfAbsent(memberId, k -> new HashMap<>()).put(category, views);
        }
        return memberDataMap;
    }

    private Map<Long, Map<Long, Double>> computeSimilarityMatrix() {
        Map<Long, Map<Long, Double>> similarityMatrix = new HashMap<>();

        for (Long memberId : memberDataMap.keySet()) {
            Map<Long, Double> memberSimilarity = new HashMap<>();
            for (Long otherMember : memberDataMap.keySet()) {
                if (!memberId.equals(otherMember)) {
                    double similarity = computeSimilarity(memberDataMap.get(memberId), memberDataMap.get(otherMember));
                    memberSimilarity.put(otherMember, similarity);
                }
            }
            // 상위 유사 사용자만 선택
            Map<Long, Double> topSimilarMembersMap = getTopNSimilarMembers(memberSimilarity, topSimilarMembers);
            similarityMatrix.put(memberId, topSimilarMembersMap);
        }
        return similarityMatrix;
    }

    // 두 사용자 간의 유사도 계산 메서드
    private double computeSimilarity(Map<Category, Integer> member1Actions, Map<Category, Integer> member2Actions) {
        // 유사도 계산에 사용될 변수 초기화
        int member1SquaredSum = 0, member2SquaredSum = 0, dotProduct = 0;

        // 공통 카테고리에 대해 유사도 계산
        for (Map.Entry<Category, Integer> entry : member1Actions.entrySet()) {
            Category category = entry.getKey();
            if (member2Actions.containsKey(category)) {
                int member1Value = entry.getValue();
                int member2Value = member2Actions.get(category);
                member1SquaredSum += (int) Math.pow(member1Value, 2);
                member2SquaredSum += (int) Math.pow(member2Value, 2);
                dotProduct += member1Value * member2Value;
            }
        }
        // 코사인 유사도 계산
        if (member1SquaredSum == 0 || member2SquaredSum == 0) {
            return 0; // 분모가 0일 경우 유사도는 0
        } else {
            return dotProduct / (Math.sqrt(member1SquaredSum) * Math.sqrt(member2SquaredSum));
        }
    }

    // 상위 유사 사용자만 선택하는 메서드
    private Map<Long, Double> getTopNSimilarMembers(Map<Long, Double> memberSimilarity, int topN) {
        Map<Long, Double> topNSimilarMembers = new HashMap<>();
        memberSimilarity.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))  // 유사도가 높은 순서대로 정렬
                .limit(topN)  // 상위 topN개의 유사 사용자만 선택
                .forEach(entry -> topNSimilarMembers.put(entry.getKey(), entry.getValue()));
        return topNSimilarMembers;
    }

    // 추천 게시글 예측 메서드
    public Map<Category, Double> predictRecommendations(long memberId) {
        Map<Category, Double> weightedSum = new HashMap<>();
        Map<Category, Double> similaritySum = new HashMap<>();
        Map<Long, Double> postSimilarity = similarityMatrix.get(memberId);

        // 상위 유사 사용자들의 가중치 합과 유사도 합 계산
        for (Map.Entry<Long, Double> entry : postSimilarity.entrySet()) {
            long otherMemberId = entry.getKey();
            double similarity = entry.getValue();

            // 해당 사용자의 평점 정보 가져오기
            Map<Category, Integer> otherMemberActions = memberDataMap.get(otherMemberId);
            for (Map.Entry<Category, Integer> action : otherMemberActions.entrySet()) {
                Category category = action.getKey();
                int value = action.getValue();
                if (!memberDataMap.get(memberId).containsKey(category) || memberDataMap.get(memberId).get(category) == 0) {
                    weightedSum.putIfAbsent(category, 0.0);
                    similaritySum.putIfAbsent(category, 0.0);
                    weightedSum.put(category, weightedSum.get(category) + similarity * value);
                    similaritySum.put(category, similaritySum.get(category) + similarity);
                } else {
                    // 사용자가 조회한 카테고리의 추천 점수를 10으로 고정
                    weightedSum.put(category, 5.0);
                    similaritySum.put(category, 1.0); // 유사도는 1로 설정하여 가중치 합을 그대로 사용
                }
            }
        }

        // 추천 점수 계산
        Map<Category, Double> recommendations = new HashMap<>();
        for (Map.Entry<Category, Double> entry : weightedSum.entrySet()) {
            Category category = entry.getKey();
            double weightedSumValue = entry.getValue();
            recommendations.put(category, weightedSumValue / similaritySum.get(category));
        }

        return recommendations;
    }

}
