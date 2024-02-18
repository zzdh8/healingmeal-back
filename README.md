
# Healing Meal Project
GDSC Solution Challenge 2024

# Member
| Backend | Frontend | PM |
| --------------- | --------------- | --------------- |
| Jinyong Hyun | Manjae Lee  | bojung Kim  |
| Inho Choi |

# Target UN-SDGs
## Goal 3. Good Health and Well-Being
<img width="351" alt="스크린샷 2024-02-15 오후 5 17 47" src="https://github.com/inhooo00/healingmeal-back/assets/129029251/c7af8650-49f7-4219-a119-04e8fd5355c0">

# About our solution
현대인들의 당뇨는 새계적으로 가장 큰 질병 중 하나로 자리잡고 있습니다. 이러한 당뇨 환자들을 위해서 식단을 제공하는 Healing Meal을 기획하였습니다.
Healing Meal은 제2 당뇨병 환자에게 맞춤식단 제공 및 개인 식단 관리를 제공하는 서비스입니다.
healing Meal은 단순히 식단 추천을 할 뿐만 아니라, 설문조사를 통해 환자의 선호에 따라 식단을 생성할 수 있습니다.
환자의 다양한 취향과 현재 신체정보를 바탕으로 합리적인 식단을 생성합니다. 나아가 식단의 효능 또한 제공합니다.

# App Demo
<img width="1470" alt="스크린샷 2024-02-19 오전 12 06 38" src="https://github.com/inhooo00/healingmeal-back/assets/129029251/22d34cfc-9b04-41a0-afba-98bbb8662333">


# About Implementation
## Backend
### Tech Stack
- JDK-17
- Spring, Spring Boot
- Spring Data JDBC & JPA
- Spring Security, Spring Session JDBC
- MySQL Connector/J
- Spring Boot Starter Mail
- JSON Simple, JSON DATA PARSING
- Google Cloud Platform
- Chatgpt Spring Boot Starter

## Frontend
### Tech Stack
- React 
- React Router
- mobX
- vite
- css module, less
- Axios

## Architecture 
![image](https://github.com/inhooo00/healingmeal-back/assets/129029251/4171d97e-3776-4c5d-bd5a-0b68a0b5ffe0)
1. vercel 클라우드 플랫폼을 통해 배포를 하였습니다.
2. Google Cloud Platform의 Infrastructure as a Service(IaaS) 제품으로, 가상 머신 인스턴스를 생성하고 관리하는 데 사용됩니다. 이를 통해 애플리케이션의 서버를 실행하고 관리하는 데 필요한 컴퓨팅 리소스를 제공받았습니다.
3. Google Cloud의 완전 관리형 SQL 데이터베이스 서비스입니다. 이를 이용하여 애플리케이션의 데이터를 저장하고 관리하였습니다.
4. Google Cloud의 로드 밸런서는 들어오는 트래픽을 여러 서버(Compute Engine 인스턴스)에 자동으로 분산시킵니다. 이를 통해 애플리케이션의 트래픽 부하를 관리하였습니다.
5. Google Cloud의 객체 스토리지 서비스로, 애플리케이션의 파일을 저장하는 데 사용되었습니다.
6. 외부 API로, 인공 지능 기술을 제공하는 API입니다. 이를 활용하여 식단의 효능에 대한 정보를 가져왔습니다 

## ERD
<img width="729" alt="스크린샷 2024-02-15 오후 5 50 39" src="https://github.com/inhooo00/healingmeal-back/assets/129029251/8ab7a46e-dfdb-4af4-ba57-5943a2821f71">

# Youtube Link
- 

# AI Guideline
- 식단을 생성하면 사용자 맞춤형으로 아침, 점심, 저녁, 간식 2개가 생성됩니다.
- 사용자가 효능을 알고 싶어서 효능 보기를 실행하면 AI가 해당 음식의 효능을 보여줍니다.
- Chatgpt Spring Boot Starter를 사용하여 구현했습니다.
- 사용자들은 식단의 효능을 따로 검색할 필요 없으므로, 더욱 만족할 것입니다.
