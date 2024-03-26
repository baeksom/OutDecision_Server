package KGUcapstone.OutDecision.domain.user.service;

import java.util.Map;

public class OAuth2UserRequest {

    private final ClientRegistration clientRegistration;
    private final OAuth2AccessToken auth2AccessToken;
    private final Map<String, Object> additionalParameters;
}
