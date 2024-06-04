package KGUcapstone.OutDecision.domain.user.controller;

import KGUcapstone.OutDecision.domain.user.dto.UpdateRequestDTO;
import KGUcapstone.OutDecision.domain.user.service.mypage.PasswordService;
import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdateController {

    private final PasswordService passwordService;

    @PatchMapping("/update/password")
    @Operation(summary = "새 비밀번호 설정", description = "새 비밀번호로 설정합니다.")
    public ApiResponse<Object> updatePassword(@RequestBody @Valid UpdateRequestDTO.UpdateNewPasswordDTO request) {
        boolean success = passwordService.updateNewPassword(request);
        if (success) return ApiResponse.onSuccess("비밀번호가 성공적으로 변경되었습니다.");
        else return ApiResponse.onFailure("400", "비밀번호 변경에 실패하였습니다.", null);
    }
}
