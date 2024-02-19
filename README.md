
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
Diabetes has established itself as one of the most significant diseases globally among modern people. 
For these diabetic patients, we have planned Healing Meal. Healing Meal is a service that provides customized diets and personal diet management for type 2 diabetes patients. 
Healing Meal does not merely recommend diets; it can create a diet based on the patient's preferences through surveys. 
It generates a reasonable diet based on the patient's various tastes and current physical information. 
Furthermore, it also provides the efficacy of the diet.

# App Demo
<img width="1470" alt="스크린샷 2024-02-19 오전 12 06 38" src="https://github.com/inhooo00/healingmeal-back/assets/129029251/22d34cfc-9b04-41a0-afba-98bbb8662333">
<img width="1045" alt="스크린샷 2024-02-19 오후 2 03 24" src="https://github.com/inhooo00/healingmeal-back/assets/129029251/d7c848dc-99a1-4699-b10e-7e0683294898">
<img width="1068" alt="스크린샷 2024-02-19 오후 2 05 15" src="https://github.com/inhooo00/healingmeal-back/assets/129029251/c4b7edca-a423-4e89-ae67-da2badfdcbb5">


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
- The deployment was done through the Vercel cloud platform.
- The Compute Engine API, a product of Google Cloud Platform's Infrastructure as a Service (IaaS), was used to create and manage virtual machine instances. This provided the necessary computing resources to run and manage the server of the application.
- Cloud SQL, a fully managed SQL database service from Google Cloud, was used to store and manage the data of the application.
- Google Cloud's load balancer, which automatically distributes incoming traffic across multiple servers (Compute Engine instances), was used to manage the application's traffic load.
- Cloud Storage, an object storage service from Google Cloud, was used to store the application's files.
- An external API, which provides artificial intelligence technology, was used to retrieve information about the efficacy of diets.

## ERD
<img width="729" alt="스크린샷 2024-02-15 오후 5 50 39" src="https://github.com/inhooo00/healingmeal-back/assets/129029251/8ab7a46e-dfdb-4af4-ba57-5943a2821f71">

# Youtube Link
- 

# AI Guideline
- When a diet is generated, it is customized for the user with breakfast, lunch, dinner, and two snacks.
- When a user wants to know the efficacy and runs the efficacy view, the AI shows the efficacy of the food.
- It was implemented using the Chatgpt Spring Boot Starter.
- Users will be more satisfied as they don't need to search separately for the efficacy of the diet.
