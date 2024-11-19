## Sử dụng hình ảnh JDK chính thức từ Docker Hub
#FROM openjdk:17-jdk-alpine
#
## Đặt thư mục làm việc bên trong container
#WORKDIR /app
#
## Copy các file Gradle hoặc Maven (để tận dụng cache nếu phụ thuộc không thay đổi)
#COPY build.gradle settings.gradle gradlew ./
#COPY gradle ./gradle
#
## Chạy lệnh build để tải các phụ thuộc (nếu có cache thì sẽ nhanh hơn)
#RUN ./gradlew build --no-daemon
#
## Copy file JAR cuối cùng vào container
#ARG JAR_FILE=build/libs/TCPharmacyBackend-0.0.1-SNAPSHOT.jar
#COPY ${JAR_FILE} app.jar
#
## Expose cổng mà ứng dụng Spring Boot sử dụng (ví dụ: 9090)
#EXPOSE 9090
#
## Chạy ứng dụng khi container bắt đầu
#ENTRYPOINT ["java", "-jar", "app.jar"]


# Stage 1: Build
FROM openjdk:17-jdk-alpine as builder

# Đặt thư mục làm việc bên trong container
WORKDIR /app

# Copy các file Gradle và các file cấu hình cần thiết
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# Copy tất cả các source code để build
COPY src ./src

# Chạy lệnh build để tạo file JAR
RUN ./gradlew build --no-daemon

# Stage 2: Run
FROM openjdk:17-jdk-alpine

# Đặt thư mục làm việc bên trong container
WORKDIR /app

# Copy file JAR từ stage build
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose cổng mà ứng dụng Spring Boot sử dụng
EXPOSE 9090

# Chạy ứng dụng khi container bắt đầu
ENTRYPOINT ["java", "-jar", "app.jar"]
