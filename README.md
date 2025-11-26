Phần Back-end của dự án đã được deploy trên Azure Cloud, người dùng chỉ cẩn tải file exe hoặc file jar để chạy và sử dụng giao diện (yêu cầu JDK17+).

# Ứng dụng Ngân hàng

Đây là một ứng dụng ngân hàng dành cho máy tính, với giao diện người dùng (front-end) được xây dựng bằng Java Swing và phần back-end sử dụng Spring Boot.

## Cấu trúc dự án

- `bankapp/`: Chứa mã nguồn back-end Spring Boot.
- `front-end/BankApp1/`: Chứa mã nguồn front-end Java Swing.

## Tính năng nổi bật

- Đăng ký tài khoản mới ([`RegisterGui`](front-end/BankApp1/src/main/java/com/bankapp/guis/RegisterGui.java))
- Đăng nhập ([`LoginGui`](front-end/BankApp1/src/main/java/com/bankapp/guis/LoginGui.java))
- Quên mật khẩu ([`ForgotPassword`](front-end/BankApp1/src/main/java/com/bankapp/guis/ForgotPassword.java))
- Xem số dư tài khoản ([`CurrentBalanceDialog`](front-end/BankApp1/src/main/java/com/bankapp/guis/CurrentBalanceDialog.java))
- Gửi tiền ([`BankingAppDialog`](front-end/BankApp1/src/main/java/com/bankapp/guis/BankingAppDialog.java))
- Rút tiền ([`BankingAppDialog`](front-end/BankApp1/src/main/java/com/bankapp/guis/BankingAppDialog.java))
- Chuyển khoản ([`BankingAppDialog`](front-end/BankApp1/src/main/java/com/bankapp/guis/BankingAppDialog.java))
- Xem lịch sử giao dịch ([`BankingAppGui`](front-end/BankApp1/src/main/java/com/bankapp/guis/BankingAppGui.java))
- Xem biểu đồ thống kê chi tiêu ([`SpendingChart`](front-end/BankApp1/src/main/java/com/bankapp/guis/SpendingChart.java))

## Hướng dẫn chạy ứng dụng

Phần Back-end của dự án đã được deploy trên Azure Cloud, người dùng chỉ cẩn tải file exe hoặc file jar để chạy và sử dụng giao diện (yêu cầu JDK).

### Back-end (Spring Boot)
Chạy local:
1. Mở thư mục `bankapp`.
2. Đảm bảo bạn đã cài đặt Maven.
3. Chạy ứng dụng bằng Maven wrapper:
    ```sh
    ./mvnw spring-boot:run
    ```
    Hoặc trên Windows:
    ```sh
    mvnw.cmd spring-boot:run
    ```
    Mặc định, back-end sẽ chạy tại địa chỉ `http://localhost:8081` (tham khảo trong [`RegisterGui`](front-end/BankApp1/src/main/java/com/bankapp/guis/RegisterGui.java)).

### Front-end (Java Swing)

1. Mở dự án `front-end/BankApp1` bằng IDE Java (NetBeans, IntelliJ IDEA, Eclipse, ...).
2. Đảm bảo đã thêm các thư viện cần thiết (các file JAR trong `front-end/BankApp1/lib/`) vào build path của dự án.
3. Tìm và chạy phương thức `main` trong lớp [`AppLauncher`](front-end/BankApp1/src/main/java/com/AppLauncher.java).

## Ảnh chụp màn hình

![Screenshot 2025-01-22 070659](https://github.com/user-attachments/assets/50e970ec-6363-4637-8ebb-79b81646f015)
![Screenshot 2025-01-22 070651](https://github.com/user-attachments/assets/6da512ed-d775-4267-ba33-1a67d1b7e8a6)
![Screenshot 2025-01-22 070640](https://github.com/user-attachments/assets/ba8a04b7-2aa8-4617-90ee-d4ef4b699e4c)
![Screenshot 2025-01-22 070711](https://github.com/user-attachments/assets/30a94430-0dbf-4066-b0a7-401d793aa87d)
![Screenshot 2025-01-22 070908](https://github.com/user-attachments/assets/ccad7031-9590-4ab1-911a-3893bf4f9239)
![Screenshot 2025-01-22 070900](https://github.com/user-attachments/assets/a357a977-0255-4f7a-9dda-2364d5f9e441)
![Screenshot 2025-01-22 070851](https://github.com/user-attachments/assets/68694f41-48d4-4c3d-a4a3-f24a881096fc)
