package KGUcapstone.OutDecision.domain.user.controller;

import KGUcapstone.OutDecision.domain.user.dto.UpdateRequestDTO.UpdateMemberDTO;
import KGUcapstone.OutDecision.domain.user.dto.UpdateRequestDTO.UpdatePasswordDTO;
import KGUcapstone.OutDecision.domain.user.dto.UpdateRequestDTO.UpdateUserImgDTO;
import KGUcapstone.OutDecision.domain.user.service.MemberService;
import KGUcapstone.OutDecision.domain.user.service.PasswordService;
import KGUcapstone.OutDecision.domain.user.service.UserImgService;
import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static KGUcapstone.OutDecision.domain.user.dto.UpdateResponseDTO.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MemberRestController {

    private final MemberService memberService;
    private final PasswordService passwordService;
    private final UserImgService userImgService;

    @GetMapping("/{memberId}/edit")
    @Operation(summary = "마이페이지 개인정보수정 조회 API", description = "마이페이지 개인정보수정 페이지에서 개인정보를 조회합니다.")
    public ApiResponse<MemberInfoDTO> getMemberInfo(@PathVariable("memberId") Long memberId) {
        MemberInfoDTO memberDTO = memberService.getMemberById(memberId);
        return ApiResponse.onSuccess(memberDTO);
    }

    @PatchMapping("/{memberId}/edit")
    @Operation(summary = "마이페이지 개인정보수정 API", description = "마이페이지 개인정보수정 페이지에서 개인정보를 수정합니다.")
    public ApiResponse<MemberInfoDTO> updateMemberInfo(@PathVariable("memberId") Long memberId, @RequestBody @Valid UpdateMemberDTO request) {
        MemberInfoDTO updateMemberDTO = memberService.updateMemberInfo(memberId, request);
        return ApiResponse.onSuccess(updateMemberDTO);
    }

    @PatchMapping("/{memberId}/edit/password")
    @Operation(summary = "마이페이지 비밀번호 변경", description = "비밀번호를 변경합니다.")
    public ApiResponse<UpdatePasswordResultDTO> updatePassword(@PathVariable("memberId") Long memberId, @RequestBody @Valid UpdatePasswordDTO request) {
        UpdatePasswordResultDTO updatePasswordResultDTO = passwordService.updatePassword(memberId, request);
        return ApiResponse.onSuccess(updatePasswordResultDTO);
    }

    @PatchMapping("/{memberId}/edit/profile")
    @Operation(summary = "마이페이지 프로필 사진 변경", description = "프로필 사진을 변경합니다.")
    public ApiResponse<UpdateUserImgResultDTO> updateUserImg(@PathVariable("memberId") Long memberId, @RequestBody @Valid UpdateUserImgDTO request) {
        UpdateUserImgResultDTO updateUserImgResultDTO = userImgService.updateUserImg(memberId, request);
        return ApiResponse.onSuccess(updateUserImgResultDTO);
    }

    @PatchMapping("/{memberId}/delete/profile")
    @Operation(summary = "마이페이지 프로필 사진 삭제", description = "프로필 사진을 삭제합니다.")
    public ApiResponse<UpdateUserImgResultDTO> deleteUserImg(@PathVariable("memberId") Long memberId) {
        UpdateUserImgResultDTO updateUserImgResultDTO = userImgService.deleteUserImg(memberId);
        return ApiResponse.onSuccess(updateUserImgResultDTO);
    }

}