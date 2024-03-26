package KGUcapstone.OutDecision.domain.user.controller;

import KGUcapstone.OutDecision.domain.user.dto.MemberResponseDTO;
import KGUcapstone.OutDecision.domain.user.service.MemberService;
import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MemberRestController {

    private final MemberService memberService;

    @GetMapping("/{memberId}/edit")
    public ApiResponse<MemberResponseDTO.MemberDTO> getMemberInfo(@PathVariable("memberId") Long memberId) {
        MemberResponseDTO.MemberDTO memberDTO = memberService.getMemberById(memberId);
        return ApiResponse.onSuccess(memberDTO);
    }
}