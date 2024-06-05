# OutDecision_Server
OutDecision - 결정 장애들을 위한 고민 투표 커뮤니티, 결정잘해(Outdecision) 🤔 Server Repo.


### 📦프로젝트 구조
```
📦 OutDesicion_Server
├─ .github
│  └─ workflows
│     └─ gradle.yml
├─ .gitignore
├─ Dockerfile
├─ README.md
├─ build.gradle
├─ gradle
│  └─ wrapper
│     ├─ gradle-wrapper.jar
│     └─ gradle-wrapper.properties
├─ gradlew
├─ gradlew.bat
├─ settings.gradle
└─ src
   ├─ main
   │  ├─ java
   │  │  └─ KGUcapstone
   │  │     └─ OutDecision
   │  │        ├─ OutDecisionApplication.java
   │  │        ├─ domain
   │  │        │  ├─ comments
   │  │        │  │  ├─ controller
   │  │        │  │  │  └─ CommentsApiController.java
   │  │        │  │  ├─ converter
   │  │        │  │  │  └─ CommentsConverter.java
   │  │        │  │  ├─ domain
   │  │        │  │  │  └─ Comments.java
   │  │        │  │  ├─ dto
   │  │        │  │  │  ├─ CommentsRequestDto.java
   │  │        │  │  │  └─ CommentsResponseDto.java
   │  │        │  │  ├─ repository
   │  │        │  │  │  └─ CommentsRepository.java
   │  │        │  │  └─ service
   │  │        │  │     └─ CommentsService.java
   │  │        │  ├─ likes
   │  │        │  │  ├─ controller
   │  │        │  │  │  └─ LikesController.java
   │  │        │  │  ├─ domain
   │  │        │  │  │  └─ Likes.java
   │  │        │  │  ├─ repository
   │  │        │  │  │  └─ LikesRepository.java
   │  │        │  │  └─ service
   │  │        │  │     ├─ LikesService.java
   │  │        │  │     └─ LikesServiceImpl.java
   │  │        │  ├─ main
   │  │        │  │  ├─ controller
   │  │        │  │  │  └─ MainRestController.java
   │  │        │  │  ├─ dto
   │  │        │  │  │  ├─ MainResponseDTO.java
   │  │        │  │  │  └─ MainResponseReDTO.java
   │  │        │  │  └─ service
   │  │        │  │     ├─ MainService.java
   │  │        │  │     ├─ MainServiceImpl.java
   │  │        │  │     ├─ MainServiceRe.java
   │  │        │  │     └─ MainServiceReImpl.java
   │  │        │  ├─ notifications
   │  │        │  │  ├─ domain
   │  │        │  │  │  └─ Notifications.java
   │  │        │  │  ├─ repository
   │  │        │  │  │  └─ NotificationsRepository.java
   │  │        │  │  └─ service
   │  │        │  │     ├─ NotificationService.java
   │  │        │  │     └─ NotificationServiceImpl.java
   │  │        │  ├─ options
   │  │        │  │  ├─ domain
   │  │        │  │  │  └─ Options.java
   │  │        │  │  └─ repository
   │  │        │  │     └─ OptionsRepository.java
   │  │        │  ├─ post
   │  │        │  │  ├─ controller
   │  │        │  │  │  ├─ PostRestController.java
   │  │        │  │  │  └─ PostsController.java
   │  │        │  │  ├─ converter
   │  │        │  │  │  └─ PostConverter.java
   │  │        │  │  ├─ domain
   │  │        │  │  │  ├─ Post.java
   │  │        │  │  │  └─ enums
   │  │        │  │  │     ├─ Category.java
   │  │        │  │  │     ├─ Gender.java
   │  │        │  │  │     └─ Status.java
   │  │        │  │  ├─ dto
   │  │        │  │  │  ├─ PostRequestDTO.java
   │  │        │  │  │  ├─ PostResponseDTO.java
   │  │        │  │  │  └─ PostsResponseDTO.java
   │  │        │  │  ├─ repository
   │  │        │  │  │  └─ PostRepository.java
   │  │        │  │  └─ service
   │  │        │  │     ├─ PostService.java
   │  │        │  │     ├─ PostServiceImpl.java
   │  │        │  │     ├─ PostsService.java
   │  │        │  │     ├─ PostsServiceImpl.java
   │  │        │  │     └─ UserBasedCF.java
   │  │        │  ├─ ranking
   │  │        │  │  ├─ controller
   │  │        │  │  │  └─ RankingRestController.java
   │  │        │  │  ├─ dto
   │  │        │  │  │  └─ RankingResponseDTO.java
   │  │        │  │  └─ service
   │  │        │  │     ├─ RankingService.java
   │  │        │  │     └─ RedisRankingServiceImpl.java
   │  │        │  ├─ title
   │  │        │  │  ├─ controller
   │  │        │  │  │  └─ MissionsRestController.java
   │  │        │  │  ├─ domain
   │  │        │  │  │  ├─ Missions.java
   │  │        │  │  │  └─ Title.java
   │  │        │  │  ├─ dto
   │  │        │  │  │  └─ MissionsResponseDTO.java
   │  │        │  │  ├─ repository
   │  │        │  │  │  ├─ MissionsRepository.java
   │  │        │  │  │  └─ TitleRepository.java
   │  │        │  │  └─ service
   │  │        │  │     ├─ MissionsService.java
   │  │        │  │     └─ MissionsServiceImpl.java
   │  │        │  ├─ user
   │  │        │  │  ├─ controller
   │  │        │  │  │  ├─ DuplicationController.java
   │  │        │  │  │  ├─ InquiryController.java
   │  │        │  │  │  ├─ LoginController.java
   │  │        │  │  │  ├─ MemberRestController.java
   │  │        │  │  │  ├─ RegisterController.java
   │  │        │  │  │  ├─ RevokeController.java
   │  │        │  │  │  ├─ TokenController.java
   │  │        │  │  │  └─ UpdateController.java
   │  │        │  │  ├─ domain
   │  │        │  │  │  ├─ Member.java
   │  │        │  │  │  └─ MemberView.java
   │  │        │  │  ├─ dto
   │  │        │  │  │  ├─ CustomUserDetails.java
   │  │        │  │  │  ├─ DuplicationRequestDto.java
   │  │        │  │  │  ├─ InquiryRequestDto.java
   │  │        │  │  │  ├─ InquiryResponseDto.java
   │  │        │  │  │  ├─ MemberResponseDTO.java
   │  │        │  │  │  ├─ RefreshToken.java
   │  │        │  │  │  ├─ RegisterRequestDto.java
   │  │        │  │  │  ├─ UpdateRequestDTO.java
   │  │        │  │  │  └─ UpdateResponseDTO.java
   │  │        │  │  ├─ repository
   │  │        │  │  │  ├─ MemberRepository.java
   │  │        │  │  │  ├─ MemberViewRepository.java
   │  │        │  │  │  └─ TokenRepository.java
   │  │        │  │  ├─ service
   │  │        │  │  │  ├─ FindMemberService.java
   │  │        │  │  │  ├─ S3Service.java
   │  │        │  │  │  ├─ auth
   │  │        │  │  │  │  ├─ CustomOAuth2UserService.java
   │  │        │  │  │  │  ├─ CustomUserDetailsService.java
   │  │        │  │  │  │  ├─ OAuth2Attribute.java
   │  │        │  │  │  │  ├─ RevokeService.java
   │  │        │  │  │  │  ├─ SaveTokenService.java
   │  │        │  │  │  │  └─ TokenService.java
   │  │        │  │  │  ├─ duplication
   │  │        │  │  │  │  ├─ DuplicationService.java
   │  │        │  │  │  │  └─ DuplicationServiceImpl.java
   │  │        │  │  │  ├─ help
   │  │        │  │  │  │  ├─ InquiryService.java
   │  │        │  │  │  │  └─ InquiryServiceImpl.java
   │  │        │  │  │  └─ mypage
   │  │        │  │  │     ├─ MemberService.java
   │  │        │  │  │     ├─ MemberServiceImpl.java
   │  │        │  │  │     ├─ MyActivityService.java
   │  │        │  │  │     ├─ MyActivityServiceImpl.java
   │  │        │  │  │     ├─ MyPageService.java
   │  │        │  │  │     ├─ MyPageServiceImpl.java
   │  │        │  │  │     ├─ PasswordService.java
   │  │        │  │  │     ├─ PasswordServiceImpl.java
   │  │        │  │  │     ├─ TitleService.java
   │  │        │  │  │     ├─ TitleServiceImpl.java
   │  │        │  │  │     ├─ UserImgService.java
   │  │        │  │  │     └─ UserImgServiceImpl.java
   │  │        │  │  └─ util
   │  │        │  │     └─ BumpsReset.java
   │  │        │  └─ vote
   │  │        │     ├─ controller
   │  │        │     │  └─ VoteController.java
   │  │        │     ├─ domain
   │  │        │     │  └─ Vote.java
   │  │        │     ├─ dto
   │  │        │     │  └─ VoteResponseDto.java
   │  │        │     ├─ repository
   │  │        │     │  └─ VoteRepository.java
   │  │        │     ├─ service
   │  │        │     │  ├─ MailService.java
   │  │        │     │  ├─ MailServiceImpl.java
   │  │        │     │  ├─ VoteService.java
   │  │        │     │  └─ VoteServiceImpl.java
   │  │        │     └─ util
   │  │        │        └─ VoteClosingTask.java
   │  │        └─ global
   │  │           ├─ common
   │  │           │  ├─ BaseEntity.java
   │  │           │  ├─ properties
   │  │           │  │  └─ JwtProperties.java
   │  │           │  └─ util
   │  │           │     ├─ AESUtil.java
   │  │           │     ├─ CookieUtil.java
   │  │           │     └─ JwtUtil.java
   │  │           ├─ config
   │  │           │  ├─ bucket
   │  │           │  │  └─ BucketConfig.java
   │  │           │  ├─ redis
   │  │           │  │  └─ RedisConfig.java
   │  │           │  ├─ security
   │  │           │  │  └─ WebConfig.java
   │  │           │  └─ swagger
   │  │           │     └─ SwaggerConfig.java
   │  │           ├─ error
   │  │           │  ├─ dto
   │  │           │  │  ├─ BaseCode.java
   │  │           │  │  ├─ BaseErrorCode.java
   │  │           │  │  ├─ ErrorReasonDTO.java
   │  │           │  │  └─ ReasonDTO.java
   │  │           │  ├─ exception
   │  │           │  │  ├─ ApiResponse.java
   │  │           │  │  ├─ ExceptionAdvice.java
   │  │           │  │  ├─ GeneralException.java
   │  │           │  │  └─ handler
   │  │           │  │     ├─ MemberHandler.java
   │  │           │  │     ├─ NotificationHandler.java
   │  │           │  │     ├─ OptionHandler.java
   │  │           │  │     └─ PostHandler.java
   │  │           │  └─ status
   │  │           │     ├─ ErrorStatus.java
   │  │           │     └─ SuccessStatus.java
   │  │           ├─ security
   │  │           │  ├─ config
   │  │           │  │  ├─ ProjectBeanConfig.java
   │  │           │  │  └─ SecurityConfig.java
   │  │           │  ├─ dto
   │  │           │  │  ├─ GeneratedToken.java
   │  │           │  │  └─ SecurityUserDto.java
   │  │           │  ├─ filter
   │  │           │  │  ├─ JwtAuthFilter.java
   │  │           │  │  └─ JwtExceptionFilter.java
   │  │           │  └─ handler
   │  │           │     ├─ CustomAuthenticationFailureHandler.java
   │  │           │     ├─ CustomAuthenticationSuccessHandler.java
   │  │           │     └─ CustomLoginSuccessHandler.java
   │  │           └─ util
   │  │              └─ DateTimeFormatUtil.java
   │  └─ resources
   │     ├─ application-app.yml
   │     ├─ application-oauth.yml
   │     ├─ application.yml
   │     ├─ email-template-notification.txt
   │     ├─ email-template.txt
   │     └─ templates
   │        ├─ login-success.html
   │        ├─ login.html
   │        ├─ normal-register.html
   │        ├─ register-success.html
   │        ├─ social-register.html
   │        └─ test.html
   └─ test
      └─ java
         └─ KGUcapstone
            └─ OutDecision
               └─ OutDecisionApplicationTests.java
```
