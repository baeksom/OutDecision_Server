package KGUcapstone.OutDecision.domain.user.auth.service;

import KGUcapstone.OutDecision.domain.user.auth.dto.RefreshToken;
import KGUcapstone.OutDecision.domain.user.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SaveTokenService {

    private final TokenRepository tokenRepository;

    @Transactional
    public void saveTokenInfo(String email, String refreshToken, String accessToken) {
        tokenRepository.save(new RefreshToken(email, accessToken, refreshToken));
    }
}