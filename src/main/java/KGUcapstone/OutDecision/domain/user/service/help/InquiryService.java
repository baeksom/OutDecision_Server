package KGUcapstone.OutDecision.domain.user.service.help;

import KGUcapstone.OutDecision.domain.user.dto.InquiryRequestDto;
import KGUcapstone.OutDecision.domain.user.dto.InquiryResponseDto;

public interface InquiryService {

    public InquiryResponseDto.InquiryPasswordResultDto inquiryPassword(InquiryRequestDto.InquiryPasswordDto request);
}
