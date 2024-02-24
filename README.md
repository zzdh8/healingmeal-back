
# Healing Meal Project
GDSC Solution Challenge 2024
<br>
- FrontEnd Repo : https://github.com/leemanjae02/HealingMeal-FrontEnd
- BackEnd Repo : https://github.com/zzdh8/healingmeal-back

# Member
| Backend      | Backend    |  Frontend  | PM          |
|------------|------------|------------|-------------|
|--------------|------------|------------|-------------|
| Jinyong Hyun | Inho Choi  | Manjae Lee | bojung Kim |
<br>

# Target UN-SDGs
## Goal 3. Good Health and Well-Being
<img width="351" alt="스크린샷 2024-02-15 오후 5 17 47" src="https://github.com/inhooo00/healingmeal-back/assets/129029251/c7af8650-49f7-4219-a119-04e8fd5355c0">

# About our solution
- Diabetes has established itself as one of the most significant diseases globally among modern people. 
- For these diabetic patients, we have planned ___**Healing Meal**___. It is a service that provides customized diets and personal diet management for type 2 diabetes patients. 
- Healing Meal does not merely recommend diets; it can create a diet based on the patient's preferences through surveys. 
- It generates a reasonable diet based on the patient's various tastes and current physical information. Furthermore, it also provides the efficacy of the diet.
- You can save and manage your preferred diet using the favorite feature.

# App Demo
![image](https://github.com/zzdh8/healingmeal-back/assets/59731570/6343855f-017c-4215-b7dd-7e83dbc2caeb)
![image](https://github.com/zzdh8/healingmeal-back/assets/59731570/fb2614ee-41d1-483e-9a80-e58af0995658)
![image](https://github.com/zzdh8/healingmeal-back/assets/59731570/6ff7b9ae-4f81-47b9-9ffe-f5a69b47f716)
![image](https://github.com/zzdh8/healingmeal-back/assets/59731570/0b2a70a6-7410-43d8-ae18-7ce9ee98368c)
![image](https://github.com/zzdh8/healingmeal-back/assets/59731570/51c66475-e3c7-40df-b7c3-f80358e15779)
![image](https://github.com/zzdh8/healingmeal-back/assets/59731570/9754d482-68e9-4489-968e-764a097f4bac)




# About Implementation
## Backend
### Tech Stack
- JDK-17
- Spring, Spring Boot
- Spring Data JDBC & JPA
- Spring Security, Spring Session JDBC
- MySQL
- Docker, Docker-compose
- JSON Simple, JSON DATA PARSING
- Spring Mail
- Spring AI
- Google Cloud Platform(compute engine, cloud sql, cloud storage, load balancer)

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

1. The Frontend Deployment was done through the Vercel cloud platform.
2. I create a Dockerfile to build an image of **HealingMeal**. And Push the image to the DockerHub.
3. The Compute Engine, an API of Google Cloud Platform, was used to create virtual machine instances.
4. Then, I create a docker-compose.yml file with informaion about Working Spring Boot(Cloud SQL, API-Key, Mail SMTP).
5. Finally, I can run a command like "docker compose up -d" to start HealingMeal application container.
6. In addition, I use Google Cloud's load balancer to manage the SSL certificate. So Everyone can access Our Service.

## ERD
<img width="729" alt="스크린샷 2024-02-15 오후 5 50 39" src="https://github.com/inhooo00/healingmeal-back/assets/129029251/8ab7a46e-dfdb-4af4-ba57-5943a2821f71">

# Introduction Video Link

# About use of LLM API
- When the diet for user is generated, it is customized for the user with breakfast, lunch, dinner, and two snacks.
- Additionally, If the user's diet information is sent through the API, the user can find out the efficacy of the diet by LLM.
- if Users read this efficacy information and prefer the diet, users can save the diet as a bookmark.
