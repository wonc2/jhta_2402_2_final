# SCM 공급망 관리 프로젝트
생산업체, 유통업체, 판매업체로 구분하여 생산품별 최저가, 선입선출을 도입하여 이익을 추구하는 밀키트 공급망 관리 프로젝트 <br><br>
## 사용 스택
<img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <br>
<img src="https://img.shields.io/badge/Java-blue?style=for-the-badge&logo=jameson&logoColor=004027">
<img src="https://img.shields.io/badge/Javascript-F7DF1E?style=for-the-badge&logo=Javascript&logoColor=white"> <img src="https://img.shields.io/badge/jquery-0769AD?style=for-the-badge&logo=jquery&logoColor=white">
<br>
<img src="https://img.shields.io/badge/mariadb-003545?style=for-the-badge&logo=mariadb&logoColor=white"> <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
<br>
<img src="https://img.shields.io/badge/thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white"> <img src="https://img.shields.io/badge/chart.js-FF6384?style=for-the-badge&logo=chartdotjs&logoColor=white"> <img src="https://img.shields.io/badge/Lombok-000000?style=for-the-badge&logo=framework&logoColor=white">
<img src="https://img.shields.io/badge/bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white">
<br>
<img src="https://img.shields.io/badge/html5-orange?style=for-the-badge&logo=html5&logoColor=white"> <img src="https://img.shields.io/badge/css3-1572B6?style=for-the-badge&logo=css3&logoColor=white">
<br>
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"> <img src="https://img.shields.io/badge/slack-4A154B?style=for-the-badge&logo=slack&logoColor=white">
<img src="https://img.shields.io/badge/notion-000000?style=for-the-badge&logo=notion&logoColor=white"> <img src="https://img.shields.io/badge/google drive-4285F4?style=for-the-badge&logo=googledrive&logoColor=white">
<br>
<img src="https://img.shields.io/badge/amazon web services-232F3E?style=for-the-badge&logo=amazonwebservices&logoColor=white"> <img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"> <br><br>


## 개발기간 
1개월 (2024.08 - 2024.09)
<br><br>

## 담당업무 
밀키트 판매 업체측에서 밀키트 주문 추가 <br>
판매측 관리자를 통해 밀키트 업체별로 매출 및 밀키트 재고 확인, 주문 상태 확인, 업체 등록 <br><br>

## ERD
![erd2](https://github.com/user-attachments/assets/4db65556-2496-4835-a027-1d19af946d6a) <br>

## 시스템 구조
![diagram](https://github.com/user-attachments/assets/5dba2f3e-4aff-4209-9e92-62edb43ad743) <br><br>

## 페이지별 기능 설명
### 판매측 관리자 페이지
1. 밀키트 주문 추가
   - 관리자는 등록되어 있는 모든 밀키트 업체에 해당하는 새로운 밀키트 주문이 추가 가능
   - 새로운 밀키트 업체 등록 가능
   - 새로운 밀키트와 그에 해당하는 재료를 등록할 수 있음 <br>
2. 밀키트 주문 및 매출 확인
   - 연월 매출액, 처리중인 주문, 처리완료된 주문 확인 가능
   - 각 밀키트 주문에 대한 정보확인, 상태변경, 취소 가능
   - 주문별로 상태가 변경될때마다 로그 확인 가능 <br>

![image](https://github.com/user-attachments/assets/380873f6-0dc2-49ed-aba6-c8c6fcc3ab88)  <br>


3. 창고
   - 밀키트 주문이 완료되면 해당 밀키트 업체에 밀키트 재고가 추가되어 창고로 이동 
   - 밀키트별 누적 판매량을 확인하여 어떤 밀키트가 많이 팔리는지 수요 예측 가능하도록 <br>

![image](https://github.com/user-attachments/assets/4c807da5-f044-4c68-a4fd-dc9fb62c751f)  <br>



4. 밀키트 가격 설정
   - 밀키트의 현재 가격과 밀키트 재료별 최소가격을 통해 밀키트의 최저가를 구하고 최저가로 설정할수 있도록함
   - 밀키트 별로 어떤 재료가 얼마나 필요한지 확인가능
   - 여러 생산 업체에서 동일한 재료를 생산할 수 있기에 생산 업체별 재료 가격을 확인하고 재료별 최저가 확인가능
   - 이를 통해 최대한의 이익을 추구하는 공급망 관리 <br>
   
![image](https://github.com/user-attachments/assets/af652027-8d8c-4f94-bd71-81b9de8d50da)
 <br>


### 판매측 사용자 페이지
- 관리자(admin)이 아닌 밀키트 업체(user)로 로그인할 경우 해당 업체에 대한 정보 확인
- 밀키트 업체의 주문 정보 확인, 취소, 새로운 주문 밀키트 주문 추가
- 밀키트 업체의 매출, 창고재고 확인 <br>
   
![image](https://github.com/user-attachments/assets/b80b4105-ef66-48fa-ba78-76fa97e1f7b3)


   
     


