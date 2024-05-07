package KGUcapstone.OutDecision.domain.user.controller;

import KGUcapstone.OutDecision.domain.user.dto.InquiryRequestDto;
import KGUcapstone.OutDecision.domain.user.dto.InquiryResponseDto;
import KGUcapstone.OutDecision.domain.user.service.help.InquiryService;
import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    @PostMapping("/help/pwInquiry")
    public ApiResponse<?> InquiryPassword(@RequestBody @Valid InquiryRequestDto.InquiryPasswordDto request) {
        InquiryResponseDto.InquiryPasswordResultDto inquiryResponseDto = inquiryService.inquiryPassword(request);
        if(inquiryResponseDto != null) {
            // 새로운 비밀번호 설정
            return ApiResponse.onSuccess(inquiryResponseDto);
        }  else {
            return ApiResponse.onFailure("400","이름 혹은 이메일 정보가 잘못 입력되었습니다.", null);
        }
    }
}
