# Hướng Dẫn Sử Dụng Backend Chat Realtime (Java Spring Boot + Socket.IO)

Dự án này cung cấp Backend cho ứng dụng Chat Realtime, bao gồm REST API cho xác thực người dùng và Socket.IO Server cho tính năng chat thời gian thực.

## 1. Yêu Cầu Hệ Thống

*   **Java**: JDK 17 trở lên.
*   **Maven**: Để quản lý dependencies và build dự án.
*   **Database**: PostgreSQL (Hiện tại đang cấu hình trỏ tới Supabase).

## 2. Cấu Hình

File cấu hình chính nằm tại: `src/main/resources/application.properties`.

### Cấu hình Database
Đảm bảo thông tin kết nối Database chính xác:
```properties
spring.datasource.url=jdbc:postgresql://<HOST>:<PORT>/<DB_NAME>
spring.datasource.username=<USERNAME>
spring.datasource.password=<PASSWORD>
```

### Cấu hình Socket.IO
Server Socket.IO chạy trên port riêng (mặc định 3001):
```properties
socket-server.host=0.0.0.0
socket-server.port=3001
```

### Cấu hình JWT
Secret key dùng để ký token (nên thay đổi khi deploy production):
```properties
jwt.key=0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF
```

## 3. Chạy Ứng Dụng

Mở terminal tại thư mục gốc của dự án và chạy lệnh:

```bash
./mvnw spring-boot:run
```

Hoặc nếu đã cài Maven toàn cục:
```bash
mvn spring-boot:run
```

Khi ứng dụng chạy thành công:
*   **REST API Server**: `http://localhost:8080`
*   **Socket.IO Server**: `localhost:3001` (ws://localhost:3001)

## 4. API Documentation

### Authentication

#### 1. Đăng Ký
*   **URL**: `/api/auth/register`
*   **Method**: `POST`
*   **Body**:
    ```json
    {
      "username": "user01",
      "password": "password123",
      "email": "user01@example.com"
    }
    ```

#### 2. Đăng Nhập
*   **URL**: `/api/auth/login`
*   **Method**: `POST`
*   **Body**:
    ```json
    {
      "username": "user01",
      "password": "password123"
    }
    ```
*   **Response**: Trả về `accessToken` dùng để xác thực Socket và các API khác.

#### 3. Lấy Thông Tin User (Me)
*   **URL**: `/api/auth/me`
*   **Method**: `GET`
*   **Headers**: `Authorization: Bearer <accessToken>`

## 5. Socket.IO Integration

Frontend (Nuxt.js/React/Vue) kết nối tới Socket server như sau:

### Client Library
Sử dụng `socket.io-client`.

### Kết Nối
```javascript
import io from 'socket.io-client';

const socket = io('http://localhost:3001', {
  query: {
    token: 'Bearer <YOUR_ACCESS_TOKEN>' // Gửi token để xác thực
  }
  // Hoặc dùng extraHeaders tùy phiên bản client
  // extraHeaders: { Authorization: 'Bearer ...' }
});

socket.on('connect', () => {
  console.log('Connected to socket server');
});
```

### Các Events

#### Client -> Server (Gửi đi)

1.  **Tham gia phòng (`join_room`)**
    ```javascript
    socket.emit('join_room', { roomId: 'general' });
    ```

2.  **Gửi tin nhắn (`send_message`)**
    ```javascript
    socket.emit('send_message', {
      roomId: 'general',
      content: 'Xin chào mọi người!',
      type: 'TEXT' // TEXT, IMAGE, FILE
    });
    ```

#### Server -> Client (Nhận về)

1.  **Nhận tin nhắn (`receive_message`)**
    ```javascript
    socket.on('receive_message', (message) => {
      console.log('Tin nhắn mới:', message);
      // message structure:
      // {
      //   id: 1,
      //   roomId: "general",
      //   sender: { id: 1, username: "...", avatar: "..." },
      //   content: "Xin chào...",
      //   timestamp: "..."
      // }
    });
    ```

2.  **Lỗi (`error`)**
    ```javascript
    socket.on('error', (err) => {
      console.error('Lỗi:', err.message);
    });
    ```

## 6. Testing

Bạn có thể dùng **Postman** để test API đăng nhập/đăng ký.
Để test Socket.IO, bạn có thể dùng **Postman** (hỗ trợ Socket.IO) hoặc **Firecamp**.

1.  Gọi API Login để lấy `accessToken`.
2.  Mở kết nối Socket.IO tới `ws://localhost:3001`.
3.  Trong phần Handshake/Query params, thêm `token` = `Bearer <accessToken>`.
4.  Connect và thử emit event `join_room` và `send_message`.
