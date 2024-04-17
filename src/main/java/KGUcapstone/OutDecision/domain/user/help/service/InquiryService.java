package KGUcapstone.OutDecision.domain.user.help.service;

import KGUcapstone.OutDecision.domain.user.help.dto.InquiryRequestDto;
import KGUcapstone.OutDecision.domain.user.help.dto.InquiryResponseDto;

public interface InquiryService {

    public InquiryResponseDto.InquiryPasswordResultDto inquiryPassword(InquiryRequestDto.InquiryPasswordDto request);
}
