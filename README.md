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
   │  │        │  │  ├─ converter
   │  │        │  │  ├─ domain
   │  │        │  │  ├─ dto
   │  │        │  │  ├─ repository
   │  │        │  │  └─ service
   │  │        │  ├─ likes
   │  │        │  │  ├─ controller
   │  │        │  │  ├─ domain
   │  │        │  │  ├─ repository
   │  │        │  │  └─ service
   │  │        │  ├─ main
   │  │        │  │  ├─ controller
   │  │        │  │  ├─ dto
   │  │        │  │  └─ service
   │  │        │  ├─ notifications
   │  │        │  │  ├─ domain
   │  │        │  │  ├─ repository
   │  │        │  │  └─ service
   │  │        │  ├─ options
   │  │        │  │  ├─ domain
   │  │        │  │  └─ repository
   │  │        │  ├─ post
   │  │        │  │  ├─ controller
   │  │        │  │  ├─ converter
   │  │        │  │  ├─ domain
   │  │        │  │  │  └─ enums
   │  │        │  │  ├─ dto
   │  │        │  │  ├─ repository
   │  │        │  │  └─ service
   │  │        │  ├─ ranking
   │  │        │  │  ├─ controller
   │  │        │  │  ├─ dto
   │  │        │  │  └─ service
   │  │        │  ├─ title
   │  │        │  │  ├─ controller
   │  │        │  │  ├─ domain
   │  │        │  │  ├─ dto
   │  │        │  │  ├─ repository
   │  │        │  │  └─ service
   │  │        │  ├─ user
   │  │        │  │  ├─ controllera
   │  │        │  │  ├─ domain
   │  │        │  │  ├─ dto
   │  │        │  │  ├─ repository
   │  │        │  │  ├─ service
   │  │        │  │  │  ├─ auth
   │  │        │  │  │  ├─ duplication
   │  │        │  │  │  ├─ help
   │  │        │  │  │  └─ mypage
   │  │        │  │  └─ util
   │  │        │  └─ vote
   │  │        │     ├─ controller
   │  │        │     ├─ domain
   │  │        │     ├─ dto
   │  │        │     ├─ repository
   │  │        │     ├─ service
   │  │        │     └─ util
   │  │        └─ global
   │  │           ├─ common
   │  │           │  ├─ BaseEntity.java
   │  │           │  ├─ properties
   │  │           │  └─ util
   │  │           ├─ config
   │  │           │  ├─ bucket
   │  │           │  ├─ redis
   │  │           │  ├─ security
   │  │           │  └─ swagger
   │  │           ├─ error
   │  │           │  ├─ dto
   │  │           │  ├─ exception
   │  │           │  │  └─ handler
   │  │           │  └─ status
   │  │           ├─ security
   │  │           │  ├─ config
   │  │           │  ├─ dto
   │  │           │  ├─ filter
   │  │           │  └─ handler
   │  │           └─ util
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
```
