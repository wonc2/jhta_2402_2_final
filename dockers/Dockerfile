# 베이스 이미지로 Ubuntu 24.04 사용
FROM ubuntu:24.04

# OpenJDK 21 및 필요한 패키지 설치
RUN apt-get update && \
    apt-get install -y wget unzip git openjdk-21-jdk fonts-nanum && \
    apt update && \
    apt install nano && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Gradle 8.x 설치
RUN wget https://services.gradle.org/distributions/gradle-8.3-bin.zip -P /tmp && \
    unzip /tmp/gradle-8.3-bin.zip -d /opt && \
    ln -s /opt/gradle-8.3/bin/gradle /usr/bin/gradle && \
    rm /tmp/gradle-8.3-bin.zip

# JAVA_HOME 설정
RUN export JAVA_HOME=$(dirname $(dirname $(readlink -f $(which java)))) && \
    echo "JAVA_HOME is set to $JAVA_HOME"

# JAVA_HOME 환경 변수 설정
ENV JAVA_HOME $JAVA_HOME
ENV PATH="$JAVA_HOME/bin:$PATH"

# 작업 디렉토리 설정
WORKDIR /app

# ARG 변수 선언 (Docker Compose와 연동하여 값을 전달)
ARG APP_DIR_NAME

# GitHub 리포지토리 클론
RUN git clone https://github.com/wonc2/jhta_2402_2_final.git ${APP_DIR_NAME}

# application.yml 복사
COPY application.yml /app/${APP_DIR_NAME}/src/main/resources/application.yml

# 클론한 디렉토리로 이동
WORKDIR /app/${APP_DIR_NAME}

# 불필요한 .git 디렉토리 제거
RUN rm -rf .git

# Gradle을 사용하여 애플리케이션 빌드 (테스트 건너뛰기)
RUN gradle build -x test

# 빌드된 JAR 파일을 실행
CMD ["java", "-jar", "build/libs/jhta_2402_2_final-0.0.1-SNAPSHOT.jar"]

# 포트 노출
EXPOSE 8081


# docker-compose --project-name kimpitang_final up -d