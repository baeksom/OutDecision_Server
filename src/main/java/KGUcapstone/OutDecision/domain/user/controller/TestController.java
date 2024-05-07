package KGUcapstone.OutDecision.domain.user.controller;

import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import KGUcapstone.OutDecision.domain.user.service.FindMemberService;
import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import KGUcapstone.OutDecision.global.security.dto.SecurityUserDto;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final FindMemberService findMemberService;
    @GetMapping("/test")
    public ApiResponse<?> test() {
        Long id = findMemberService.findLoginMemberId();
        if (id != 0L) {
            return ApiResponse.onSuccess(id);
        }
        return ApiResponse.onFailure("400", "사용자가 없습니다.", id);
    }
}