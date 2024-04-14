package KGUcapstone.OutDecision.domain.title.controller;

import KGUcapstone.OutDecision.domain.title.dto.MissionsResponseDTO;
import KGUcapstone.OutDecision.domain.title.dto.MissionsResponseDTO.MemberMissionsDTO;
import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MissionsRestController {

    @GetMapping("/{memberId}/title/missions")
    @Operation(summary = "마이페이지 칭호 미션 조회 API", description = "사용자가 칭호를 얻기 위한 미션 진행도를 조회하는 API입니다.")
    public ApiResponse<MemberMissionsDTO> getMemberMissions(@PathVariable(name = "memberId") Long memberId) {

        return ApiResponse.onSuccess();
    }
}
