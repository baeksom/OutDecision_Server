package KGUcapstone.OutDecision.domain.user.controller;

import KGUcapstone.OutDecision.domain.user.dto.DuplicationRequestDto;
import KGUcapstone.OutDecision.domain.user.service.duplication.DuplicationService;
import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/duplication")
public class DuplicationController {

    private final DuplicationService duplicateService;

    @PostMapping("/email")
    public ApiResponse<Object> emailDuplicationCheck(@RequestBody @Valid DuplicationRequestDto.CheckDto request) {
        if(duplicateService.checkEmailExist(request.getRequest())) return ApiResponse.onFailure("400", "이미 존재하는 메일 입니다.", null);
        else return ApiResponse.onSuccess(null);
    }

    @PostMapping("/nickname")
    public ApiResponse<Object> nicknameDuplicationCheck(@RequestBody @Valid DuplicationRequestDto.CheckDto request) {
        if(duplicateService.checkNicknameExist(request.getRequest())) return ApiResponse.onFailure("400", "이미 존재하는 닉네임 입니다.", null);
        else return ApiResponse.onSuccess(null);
    }
}
