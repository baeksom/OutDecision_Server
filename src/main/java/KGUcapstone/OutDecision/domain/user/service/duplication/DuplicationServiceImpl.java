package KGUcapstone.OutDecision.domain.user.service.duplication;

import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DuplicationServiceImpl implements DuplicationService {
    private final MemberRepository memberRepository;

    public boolean checkEmailExist(String request) {
        return Optional.ofNullable(memberRepository.findByEmail(request)).isPresent();
    }

    public boolean checkNicknameExist(String request) {
        return Optional.ofNullable(memberRepository.findByNickname(request)).isPresent();
    }

}