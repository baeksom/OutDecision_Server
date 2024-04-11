package KGUcapstone.OutDecision.domain.post.domain;

import java.sql.*;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

public class RecommendSystem {

    private static final Logger logger = Logger.getLogger(RecommendSystem.class.getName());
    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        try {
            // MySQL 데이터베이스에 연결
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Outdecision", "root", "0000");
            statement = connection.createStatement();

            // Post 테이블에서 데이터 가져오기
            ResultSet postDataResult = statement.executeQuery("SELECT * FROM post");
            // board_data 테이블에서 데이터 가져오기
            Map<String, List<Map<String, Integer>>> postData = new HashMap<>();
            while (postDataResult.next()) {
                String category = postDataResult.getString("category");
                long postId = postDataResult.getLong("id");
                int likes = postDataResult.getInt("likes");
                int views = postDataResult.getInt("views");
                postData.computeIfAbsent(category, k -> new ArrayList<>()).add(
                        new HashMap<>() {{
                            put("게시글 번호", (int) postId);
                            put("추천 수", likes);
                            put("조회수", views);
                        }}
                );
            }

            // MemberView 테이블에서 데이터 가져오기
            ResultSet memberViewDataResult = statement.executeQuery("SELECT * FROM member_view");
            Map<Long, Map<String, Integer>> memberViewData = new HashMap<>();
            while (memberViewDataResult.next()) {
                Long memberId = memberViewDataResult.getLong("member_id");
                String category = memberViewDataResult.getString("category");
                int postNumber = memberViewDataResult.getInt("viewsCount");
                memberViewData.computeIfAbsent(memberId, k -> new HashMap<>()).put(category, postNumber);
            }

            // 사용자 기반 협업 필터링 모델 초기화
            int topSimilarUsers = 5; // 상위 유사 사용자의 수 설정
            UserBasedCF userBasedCF = new UserBasedCF(memberViewData, topSimilarUsers);
            long memberId = 1; // 사용자 ID 설정
            Map<String, Double> recommendations = userBasedCF.predictRecommendations(memberId);

            // 해당 사용자의 추천 게시글 예측 점수 출력
            System.out.println(memberId + "의 추천 게시글 예측 점수:");
            for (Map.Entry<String, Double> entry : recommendations.entrySet()) {
                System.out.println("카테고리: " + entry.getKey() + ", 예측 점수: " + String.format("%.2f", entry.getValue()));
            }

            // 해당 사용자의 맞춤 추천 게시글 출력
            System.out.println("\n" + memberId + "의 맞춤 추천 게시글:");

            List<Map<String, Integer>> allPosts = new ArrayList<>();
            for (List<Map<String, Integer>> posts : postData.values()) {
                allPosts.addAll(posts);
            }

            // 게시글 평점 계산 및 정렬
            allPosts.sort((post1, post2) -> {
                String category1 = post1.get("category") != null ? post1.get("category").toString() : "";
                String category2 = post2.get("category") != null ? post2.get("category").toString() : "";
                double score1 = recommendations.getOrDefault(category1, 0.0) * 20 + post1.get("추천 수") * 5 + post1.get("조회수");
                double score2 = recommendations.getOrDefault(category2, 0.0) * 20 + post2.get("추천 수") * 5 + post2.get("조회수");
                return Double.compare(score2, score1); // 내림차순 정렬
            });

            // 중복을 피하기 위한 Set
            Set<Integer> seenPostNumbers = new HashSet<>();
            List<Integer> recommendationPostNumbers = new ArrayList<>();

            // 상위 5개 게시글 선택
            for (Map<String, Integer> post : allPosts) {
                int postId = post.get("게시글 번호");
                if (!seenPostNumbers.contains(postId)) {
                    recommendationPostNumbers.add(postId);
                    seenPostNumbers.add(postId);
                }
                if (recommendationPostNumbers.size() == 5) {
                    break;
                }
            }

            System.out.println(recommendationPostNumbers); // 수정된 맞춤 추천 게시글 출력

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "An error occurred", e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "An error occurred", e);
            }
        }
    }

}
