# 🌱 푸동푸동

AI 트레이너와 함께하는 러닝 도움 헬스케어 애플리케이션

## 기술 스택

### Backend
- Java 17
- Spring Boot 3.5.4
- Spring Security
- Spring Data JPA, QueryDSL
- WebFlux
- MySQL/AWS RDS
- Swagger

### 외부 API
- Kakao Map API
- Kakao Navi API
- Google Gemini API


## 프로젝트 구조

```
  src/main/java/purureum/pudongpudong/
  ├── PudongpudongApplication.java          
  ├── application/                          # 애플리케이션 계층
  │   └── service/
  │       ├── AuthService.java             # 인증 서비스
  │       ├── VoiceAssistantService.java    # AI 음성 어시스턴트
  │       ├── command/                      # 명령 서비스 (CUD)
  │       │   ├── ParkCommandService.java
  │       │   └── RunningCommandService.java
  │       └── query/                        # 조회 서비스 (R)
  │           ├── CollectionQueryService.java
  │           ├── HomeQueryService.java
  │           ├── ParkQueryService.java
  │           └── RunningQueryService.java
  ├── domain/                               # 도메인 계층
  │   ├── model/                           # 엔티티
  │   │   ├── Users.java                   # 사용자
  │   │   ├── Parks.java                   # 공원
  │   │   ├── Species.java                 # 동물 종
  │   │   ├── Trainers.java               # 트레이너
  │   │   ├── Sessions.java               # 운동 세션
  │   │   ├── UserStamps.java             # 사용자 스탬프
  │   │   ├── UserStatistics.java         # 사용자 통계
  │   │   └── enums/                       
  │   └── repository/                      # 레포지토리
  ├── infrastructure/                       
  │   ├── adapter/
  │   │   ├── api/                         # 외부 API 어댑터
  │   │   │   ├── GeminiApiService.java
  │   │   │   ├── KakaoMapApiService.java
  │   │   │   └── KakaoNaviApiService.java
  │   │   └── controller/                  # REST 컨트롤러
  │   ├── batch/                           # 배치 작업
  │   ├── dto/                             
  │   └── web/
  └── global/                              # 공통 설정
      ├── apiPayload/                      # API 응답 구조
      ├── common/                          # 공통 도메인
      ├── config/                          # 설정 클래스
      └── util/                            # 유틸리티
```


## 시스템 아키텍처

```
       [Web Client] 
           ↕ HTTP/HTTPS
[Spring Boot API Server (Java 17)]
     ├─ Spring Security + JWT
     ├─ Swagger
     └─ Docker Container
           ↕
    [MySQL Database (AWS RDS)]
           ↕
      [External APIs]
     ├─ Kakao Map API
     ├─ Kakao Navi API  
     └─ Google Gemini AI API
```


## 애플리케이션 소개
![푸동푸동_발표-이미지-1](https://github.com/user-attachments/assets/6c75a457-5f83-4f47-939a-0864dfda0f10)
![푸동푸동_발표-이미지-2](https://github.com/user-attachments/assets/a6828553-f324-4961-924d-14d56041b2e8)
![푸동푸동_발표-이미지-3](https://github.com/user-attachments/assets/8eb57bf7-0de4-453a-8eac-db8cb0d9e9b6)
![푸동푸동_발표-이미지-4](https://github.com/user-attachments/assets/0da26815-0783-41b7-b578-b664e2440701)
![푸동푸동_발표-이미지-5](https://github.com/user-attachments/assets/ee950898-752d-49c3-b4aa-0faa7ee26882)
![푸동푸동_발표-이미지-6](https://github.com/user-attachments/assets/8958436c-191d-4873-a340-5114b97aa0f8)
![푸동푸동_발표-이미지-7](https://github.com/user-attachments/assets/aec4d3e6-ce57-4172-90ef-dbfc178e84de)
![푸동푸동_발표-이미지-8](https://github.com/user-attachments/assets/878c31a3-a35d-4cdc-845b-9be6722db473)
![푸동푸동_발표-이미지-9](https://github.com/user-attachments/assets/fad791d3-c816-4862-868e-3449d9a20165)
![푸동푸동_발표-이미지-10](https://github.com/user-attachments/assets/29b233f7-2064-498b-a29b-edf73064f2a1)

## API
[API 명세서](https://noturss.notion.site/API-245837611aad80f99595f83349517a59?source=copy_link)
<img width="964" height="646" alt="Screenshot 2025-08-30 at 08 02 52" src="https://github.com/user-attachments/assets/21064c7d-7e9c-446b-a882-26fbb39ecec8" />


## 팀원 구성

| 이름                                                                                                                                               | 역할      | 담당 기능                          |
|--------------------------------------------------------------------------------------------------------------------------------------------------|-------------|------------------------------------|
| [<img src="https://avatars.githubusercontent.com/u/96182623?v=4" height=130 width=130> <br/> @tl1l1l1s](https://github.com/tl1l1l1s) **신윤지**     | 백엔드 및 프론트엔드  | 백엔드 전체, 프론트엔드 캘린더/컬렉션 화면 API 연동, 러닝 중 음성 인식 및 음성 합성 기능 전체|
| [<img src="https://avatars.githubusercontent.com/u/163801908?v=4" height=130 width=130> <br/> @gaeunee2](https://github.com/gaeunee2) **김가은**     | 팀장, 프론트엔드  | 기획, 디자인 및 프론트엔드|
| [<img src="https://avatars.githubusercontent.com/u/156641278?v=4" height=130 width=130> <br/> @lucy7noh](https://github.com/lucy7noh) **노윤선**     | 프론트엔드  | 기획, 디자인 및 프론트엔드|

