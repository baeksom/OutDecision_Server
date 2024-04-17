package KGUcapstone.OutDecision.domain.user.help.service;

import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.help.dto.InquiryRequestDto;
import KGUcapstone.OutDecision.domain.user.help.dto.InquiryResponseDto;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService{

    private final MemberRepository memberRepository;

    @Override
    public InquiryResponseDto.InquiryPasswordResultDto inquiryPassword(InquiryRequestDto.InquiryPasswordDto request) {
        String email = request.getEmail();
        String name = request.getName();

        Member member = memberRepository.findByEmail(email);

        if(member != null && member.getName().equals(name)) {
            // 비밀번호 변경
            return InquiryResponseDto.InquiryPasswordResultDto.builder()
                    .memberId(member.getId())
                    .build();
        }
        return null;
    }
}
