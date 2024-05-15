package KGUcapstone.OutDecision.domain.main.controller;

import KGUcapstone.OutDecision.domain.main.dto.MainResponseReDTO.PostListReDTO;
import KGUcapstone.OutDecision.domain.main.service.MainServiceRe;
import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class MainRestReController {
    private final MainServiceRe mainServicere;

    @GetMapping("/recommend")
    @Operation(summary = "메인페이지 추천 게시물 조회 API", description = "메인페이지로 추천 게시물 섹션으로 이루어져 있습니다.")
    public ApiResponse<PostListReDTO> getMainRePostList() {
        return ApiResponse.onSuccess(mainServicere.getMainRe());
    }
}
