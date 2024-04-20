package KGUcapstone.OutDecision.domain.post.controller;

import KGUcapstone.OutDecision.domain.post.dto.PostsResponseDto;
import KGUcapstone.OutDecision.domain.post.service.PostsService;
import KGUcapstone.OutDecision.domain.post.service.PostsServiceImpl;
import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import io.swagger.models.auth.In;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class PostsController {

    private final PostsService postsService;

    @GetMapping("/posts/{category}")
    @Operation(summary = "게시판 조회 API", description = "필터링 및 검색 기능을 포함한 게시글을 조회합니다.")
    public ApiResponse<Page<PostsResponseDto.PostDto>> getPosts (@PathVariable String category,
                                                                 @RequestParam String mode,
//                          @RequestParam String type,
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

        Page<PostsResponseDto.PostDto> postDtoPage = postsService.getPosts(sort, search, searchType, filters, page-1, 10);
        return ApiResponse.onSuccess(postDtoPage);
    }

}
