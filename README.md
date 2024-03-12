# OutDecision_Server
OutDecision - 결정 장애들을 위한 고민 투표 커뮤니티, 결정잘해(Outdecision) 🤔 Server Repo.

\n\n
### 📦프로젝트 구조
```
📦 OutDesicion_Server
├─ .gitignore
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
   │  │        │  └─ Temp.java
   │  │        └─ global
   │  │           ├─ common
   │  │           │  └─ BaseEntity.java
   │  │           ├─ config
   │  │           │  ├─ security
   │  │           │  │  └─ WebConfig.java
   │  │           │  └─ swagger
   │  │           │     └─ SwaggerConfig.java
   │  │           └─ error
   │  │              ├─ dto
   │  │              │  ├─ BaseCode.java
   │  │              │  ├─ BaseErrorCode.java
   │  │              │  ├─ ErrorReasonDTO.java
   │  │              │  └─ ReasonDTO.java
   │  │              ├─ exception
   │  │              │  ├─ ApiResponse.java
   │  │              │  ├─ ExceptionAdvice.java
   │  │              │  └─ GeneralException.java
   │  │              ├─ handler
   │  │              │  └─ TempHandler.java
   │  │              └─ status
   │  │                 ├─ ErrorStatus.java
   │  │                 └─ SuccessStatus.java
   │  └─ resources
   │     └─ application.yml
   └─ test
      └─ java
         └─ KGUcapstone
            └─ OutDecision
               └─ OutDecisionApplicationTests.java
```

