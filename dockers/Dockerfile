# 베이스 이미지로 Ubuntu 24.04 사용
FROM ubuntu:24.04

# OpenJDK 21, git 및 기타 패키지 설치
RUN apt-get update && \
    apt-get install -y wget unzip git openjdk-21-jdk fonts-nanum nano && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# 환경 변수 (.env 에서 설정)
ARG APP_DIR_NAME
ARG GIT_REPO_URL
ARG DOCKER_FOLDER_YML_PATH
ARG CONTAINER_YML_PATH

# git clone & application.yml 복사 ( docker_folder에 application.yml 미리 만들어야 복사됨 )
WORKDIR /app
RUN git clone ${GIT_REPO_URL} ${APP_DIR_NAME}
RUN rm -rf /app/${APP_DIR_NAME}/.git
COPY ${DOCKER_FOLDER_YML_PATH} ${CONTAINER_YML_PATH}

# git clone 받은 파일로 이동 & Gradle Wrapper 빌드
WORKDIR /app/${APP_DIR_NAME}
RUN chmod +x gradlew && ./gradlew build -x test