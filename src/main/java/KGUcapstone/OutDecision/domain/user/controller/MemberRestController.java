package KGUcapstone.OutDecision.domain.user.controller;

import KGUcapstone.OutDecision.domain.user.dto.MemberRequestDTO;
import KGUcapstone.OutDecision.domain.user.dto.MemberResponseDTO;
import KGUcapstone.OutDecision.domain.user.service.MemberService;
import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MemberRestController {

    private final MemberService memberService;

    @GetMapping("/{memberId}/edit")
    @Operation(summary = "마이페이지 개인정보수정 조회 API", description = "마이페이지 개인정보수정 페이지에서 개인정보를 조회합니다.")
    public ApiResponse<MemberResponseDTO.MemberDTO> getMemberInfo(@PathVariable("memberId") Long memberId) {
        MemberResponseDTO.MemberDTO memberDTO = memberService.getMemberById(memberId);
        return ApiResponse.onSuccess(memberDTO);
    }

    @PatchMapping("/{memberId}/edit")
    @Operation(summary = "마이페이지 개인정보수정 API", description = "마이페이지 개인정보수정 페이지에서 개인정보를 수정합니다.")
    public ApiResponse<MemberResponseDTO.MemberDTO> updateMemberInfo(@PathVariable("memberId") Long memberId, @RequestBody @Valid MemberRequestDTO.UpdateMemberDTO requset) {
        MemberResponseDTO.MemberDTO updateMemberDTO = memberService.updateMemberInfo(memberId, requset);
        return ApiResponse.onSuccess(updateMemberDTO);
    }

}