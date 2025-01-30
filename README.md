UX 테스트용 앱의 백엔드 레포지토리 입니다.
* 프론트엔드 레포지토리 : https://github.com/num22yh/UX-TEST-FRONT-APP
  
# 개요
- 이 앱은 사용자에게 효과적으로 정보를 전달하는 방식을 탐색하는 연구의 일환으로 개발되었습니다.  
- 검색, 규칙 기반 챗봇, 대화형 챗봇에서의 사용자 경험 차이를 비교하는 것을 목표로 합니다.

## 주요 기능

| 🔍 검색 | 🤖 규칙 기반 챗봇 | 💬 대화형 챗봇 |
|---------|-----------------|----------------|
| 사용자가 검색을 통해 관련된&nbsp;문서들을 직접 확인하는 방식입니다. | 미리 정해진 규칙을 기반으로 선택지가 제공되며, 사용자는 선택지 중 하나를 선택하며 정보를 탐색합니다. | 사용자가 자연어로 질문하면 모델이(OpenAI의 GPT-3.5-Turbo를 Fine-tuning하여 사용) 자동으로 응답을 생성합니다. |
|<img src="https://github.com/user-attachments/assets/0cad9822-34f4-41bd-8bfd-90b97bfeda8d" width="200"/> |<img src="https://github.com/user-attachments/assets/c1ae113e-756f-4853-a63b-65a05c8e6424" width="200"/> | <img src="https://github.com/user-attachments/assets/e734f3d2-aa32-471b-8d38-0f724101304b" width="200"/> |

---

## 🛠 개발 참여 인원
- **프론트엔드 개발자** 1명
- **백엔드 개발자** 1명

---

## 💻 기술 스택

### 시스템 아키텍처
<img src="https://github.com/user-attachments/assets/4ef355ed-4dce-4fe5-8702-53872c181834"/>

### Frontend
- **Language**: JavaScript (Node.js v20.10.0, npm 10.2.3)
- **Framework**: React Native, Expo

### Backend
- **Language**: Java 17
- **Framework**: Spring Boot 3.1.7

### Database
- **MySQL Community Edition 8.0**

---

## 🌍 사용된 API

- **Google Cloud Translation API**  
  [Google Cloud Translation API](https://cloud.google.com/translate/?hl=ko)
  
- **OpenAI Fine-Tuning API**  
  [OpenAI Fine-Tuning API](https://platform.openai.com/docs/guides/fine-tuning)

---
