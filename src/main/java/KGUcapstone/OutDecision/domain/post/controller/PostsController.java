package KGUcapstone.OutDecision.domain.post.controller;

import KGUcapstone.OutDecision.domain.post.converter.PostConverter;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.service.PostsService;
import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static KGUcapstone.OutDecision.domain.post.dto.PostsResponseDTO.*;

@RestController
@RequiredArgsConstructor
public class PostsController {

    private final PostsService postsService;

    @GetMapping("/posts/{category}")
    @Operation(summary = "게시판 조회 API", description = "필터링 및 검색 기능을 포함한 게시글을 조회합니다.")
    public ApiResponse<PostListDTO> getPosts (@PathVariable String category,
                                                @RequestParam String mode,
                                                @RequestParam Integer page,
                                                @RequestParam(required = false) String gender,
                                                @RequestParam(required = false) String vote,
                                                @RequestParam(required = false) String search,
                                                @RequestParam(name = "search-type", required = false) String searchType,
                                                @RequestParam String sort) {
        Map<String, String> filters = new HashMap<>();
        filters.put("category", category);
        filters.put("mode", mode);
        filters.put("gender", gender);
        filters.put("vote", vote);

        Page<Post> postPage = postsService.getPosts(sort, search, searchType, filters, page-1, 6);
        return ApiResponse.onSuccess(PostConverter.toPostListDTO(postPage));
    }

    @GetMapping("/posts")
    @Operation(summary = "전체 게시판 조회 API", description = "필터링 및 검색 기능을 포함한 전체 게시글을 조회합니다.")
    public ApiResponse<PostListDTO> getAllPosts (@RequestParam String mode,
                                              @RequestParam Integer page,
                                              @RequestParam(required = false) String gender,
                                              @RequestParam(required = false) String vote,
                                              @RequestParam(required = false) String search,
                                              @RequestParam(name = "search-type", required = false) String searchType,
                                              @RequestParam String sort) {
        Map<String, String> filters = new HashMap<>();
        filters.put("mode", mode);
        filters.put("gender", gender);
        filters.put("vote", vote);

        Page<Post> postPage = postsService.getPosts(sort, search, searchType, filters, page-1, 6);
        return ApiResponse.onSuccess(PostConverter.toPostListDTO(postPage));
    }

}
