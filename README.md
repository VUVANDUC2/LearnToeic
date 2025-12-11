# Learn TOEIC (Spring Boot + Thymeleaf)

## Giới thiệu
Learn TOEIC là ứng dụng web luyện thi TOEIC với Spring Boot + Thymeleaf, hỗ trợ làm đề có âm thanh/hình ảnh, flashcard, blog và trang quản trị nội dung.

## Tính năng nổi bật
- `Luyện đề TOEIC`: bắt đầu/tiếp tục bài làm, hiển thị từng Part kèm audio/hình, lưu đáp án, chấm điểm và xem lại kết quả.
- `Thư viện đề thi`: phân trang, tìm kiếm; admin bật/tắt trạng thái đề, xem trước câu hỏi, tải lên bộ file audio/hình/CSV cho từng đề.
- `Flashcard`: tạo/chia sẻ bộ từ vựng, luyện từ mới/ôn tập, chỉnh sửa/xóa nhanh, import/export CSV.
- `Blog/kiến thức`: trang danh sách + chi tiết, quản trị tạo/sửa/xóa; hỗ trợ nhập bài từ file .docx, trích xuất excerpt/thumbnail tự động.
- `Quản trị`: dashboard quản lý đề thi, câu hỏi, người dùng, blog, thư mục tài nguyên/upload.
- `Hồ sơ & xác thực`: đăng ký/đăng nhập, đăng nhập Google OAuth2, quên mật khẩu qua email (token JWT), cập nhật hồ sơ + avatar, xem lịch sử làm bài.

## Công nghệ & phụ thuộc chính
- Java 17, Spring Boot 3.5, Maven
- Spring MVC + Thymeleaf
- Spring Security (form login + OAuth2 Google) và JWT cho đặt lại mật khẩu
- Spring Data JPA + Hibernate với MySQL 8
- Mail (SMTP), Lombok
- Docx4j + Jsoup để import .docx thành HTML và xử lý ảnh nhúng
- OpenCSV, Apache Commons IO cho import/export và thao tác tệp

## Chuẩn bị
- JDK 17+
- Maven 3.9+ (hoặc dùng `mvnw` đi kèm)
- MySQL 8 và một database trống, ví dụ `learntoeic`
- (Tuỳ chọn) Gmail App Password và thông tin OAuth2 Google để đăng nhập nhanh

## Cấu hình ứng dụng
Cập nhật `src/main/resources/application.properties` hoặc đặt biến môi trường tương đương:
```
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/learntoeic?allowPublicKeyRetrieval=true&sslMode=DISABLED&serverTimezone=UTC
SPRING_DATASOURCE_USERNAME=your_user
SPRING_DATASOURCE_PASSWORD=your_password
SPRING_JPA_HIBERNATE_DDL_AUTO=none

SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=your_gmail
SPRING_MAIL_PASSWORD=your_app_password

SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_ID=your_google_client_id
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_SECRET=your_google_client_secret

UPLOADS_BASE_PATH=uploads
FILE_UPLOAD_DIR=./data/uploads/tests
```

## Thiết lập nhanh (dev)
1. Clone repo và `cd` vào thư mục `LearnToeic`.
2. Tạo database và nạp dữ liệu mẫu (nếu cần): `mysql -u <user> -p learntoeic < mydb.sql`.
3. Điền thông tin DB/SMTP/OAuth2 theo phần cấu hình; đảm bảo thư mục `uploads` và `data/uploads/tests` có quyền ghi.
4. Chạy ứng dụng: `./mvnw spring-boot:run`.
5. Truy cập `http://localhost:8080`.

## Build & kiểm thử
- Đóng gói: `./mvnw clean package` (tạo file `target/LearnToeic-0.0.1-SNAPSHOT.jar`).
- Chạy tests: `./mvnw test`.

## Cấu trúc thư mục chính
- `src/main/java/LearnToeic` – controllers (web/admin/API), services (Exam, Flashcard, Blog, Mail...), cấu hình bảo mật.
- `src/main/resources/templates` – view Thymeleaf (Home, tests, blog, auth, profile, admin...).
- `src/main/resources/static` – CSS/JS/images và dữ liệu audio/hình mẫu.
- `uploads/` – nơi lưu file người dùng tải lên (audio, hình, CSV); nên đặt trên ổ đĩa có quyền ghi.
- `mydb.sql` – script schema/dữ liệu mẫu cho MySQL.
- `pom.xml` – cấu hình Maven và dependencies.

## Lưu ý triển khai
- Không check-in mật khẩu DB/SMTP; dùng biến môi trường/secret manager cho production.
- Gmail cần App Password; Google OAuth2 phải cấu hình redirect `http://localhost:8080/login/oauth2/code/google` (điều chỉnh theo host khi deploy).
- Khi triển khai thật, bật cache Thymeleaf và cấu hình log/SSL phù hợp.
