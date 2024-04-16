package KGUcapstone.OutDecision.domain.user.auth.controller;

import KGUcapstone.OutDecision.domain.user.auth.service.RevokeService;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.service.MemberService;
import KGUcapstone.OutDecision.global.common.util.JwtUtil;
import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RevokeController {

    private final RevokeService revokeService;

    @DeleteMapping("/user/revoke")
    public ApiResponse<Object> revokeMemberController(@RequestHeader("Authorization") String accessToken) throws IOException {
        revokeService.deleteAccount(accessToken);
        return ApiResponse.onSuccess(null);
    }
}
