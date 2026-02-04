FROM eclipse-temurin:21-jre

## 실행 경로
WORKDIR /app/myce-backend/notification

## 실행 파일
COPY ./api/build/libs/*.jar myce-notification.jar

## 실행 포트
EXPOSE 8011

ENTRYPOINT ["java", "-jar", "myce-notification.jar", "--spring.profiles.active=product"]