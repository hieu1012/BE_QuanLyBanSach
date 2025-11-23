package iuh.fit.controllers;

import iuh.fit.dtos.user.LoginRequest;
import iuh.fit.dtos.user.RegisterRequest;
import iuh.fit.dtos.user.ForgotPasswordRequest;
import iuh.fit.dtos.user.ResetPasswordRequest;
import iuh.fit.entities.User;
import iuh.fit.entities.enums.Role;
import iuh.fit.exceptions.UnauthorizedException;
import iuh.fit.repositories.UserRepository;
import iuh.fit.utils.JwtUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Authentication Controller
 * Xử lý login/logout và JWT token management
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * POST /api/auth/login
     * Xác thực username/password và trả về JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Kiểm tra user tồn tại
            User user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new UnauthorizedException("Username hoặc password không đúng"));

            // Kiểm tra password
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                throw new UnauthorizedException("Username hoặc password không đúng");
            }

            // Kiểm tra user có active không
            if (!user.getIsActive()) {
                throw new UnauthorizedException("Tài khoản của bạn đã bị khóa");
            }

            // Generate JWT token
            String accessToken = jwtUtil.generateToken(user);
            String refreshToken = jwtUtil.generateRefreshToken(user);

            // Tạo response
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Đăng nhập thành công");
            
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("accessToken", accessToken);
            data.put("refreshToken", refreshToken);
            data.put("tokenType", "Bearer");
            data.put("user", new UserLoginResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getFullName(),
                    user.getRole().toString()
            ));
            
            response.put("data", data);

            logger.info("User {} logged in successfully", loginRequest.getUsername());
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (UnauthorizedException e) {
            Map<String, Object> errorResponse = new LinkedHashMap<>();
            errorResponse.put("status", HttpStatus.UNAUTHORIZED.value());
            errorResponse.put("message", e.getMessage());
            errorResponse.put("error", "UNAUTHORIZED");
            
            logger.warn("Login failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    /**
     * POST /api/auth/register
     * Đăng ký tài khoản mới
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            // Kiểm tra password và confirmPassword có khớp không
            if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
                Map<String, Object> errorResponse = new LinkedHashMap<>();
                errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
                errorResponse.put("message", "Password và Confirm Password không khớp");
                errorResponse.put("error", "VALIDATION_ERROR");
                
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            // Kiểm tra username đã tồn tại chưa
            if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
                Map<String, Object> errorResponse = new LinkedHashMap<>();
                errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
                errorResponse.put("message", "Username đã tồn tại");
                errorResponse.put("error", "VALIDATION_ERROR");
                
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            // Kiểm tra email đã tồn tại chưa
            if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
                Map<String, Object> errorResponse = new LinkedHashMap<>();
                errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
                errorResponse.put("message", "Email đã tồn tại");
                errorResponse.put("error", "VALIDATION_ERROR");
                
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            // Tạo user mới
            User newUser = new User();
            newUser.setUsername(registerRequest.getUsername());
            newUser.setEmail(registerRequest.getEmail());
            newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            newUser.setFullName(registerRequest.getFullName());
            newUser.setPhoneNumber(registerRequest.getPhoneNumber());
            newUser.setAddress(registerRequest.getAddress());
            newUser.setRole(Role.USER); // Mặc định là USER
            newUser.setIsActive(true);

            // Lưu user vào database
            User savedUser = userRepository.save(newUser);

            // Tạo response
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("status", HttpStatus.CREATED.value());
            response.put("message", "Đăng ký thành công");
            
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("id", savedUser.getId());
            data.put("username", savedUser.getUsername());
            data.put("email", savedUser.getEmail());
            data.put("fullName", savedUser.getFullName());
            data.put("role", savedUser.getRole().toString());
            
            response.put("data", data);

            logger.info("User {} registered successfully", registerRequest.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new LinkedHashMap<>();
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("message", "Lỗi khi đăng ký: " + e.getMessage());
            errorResponse.put("error", "INTERNAL_SERVER_ERROR");
            
            logger.error("Registration failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * POST /api/auth/forgot-password
     * Gửi reset token qua email để reset password
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, Object>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        try {
            // Tìm user theo email
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UnauthorizedException("Email không tồn tại trong hệ thống"));

            // Tạo reset token
            String resetToken = UUID.randomUUID().toString();
            LocalDateTime resetTokenExpiry = LocalDateTime.now().plusHours(24); // Token có hiệu lực 24 giờ

            // Lưu reset token và expiry vào database
            user.setResetToken(resetToken);
            user.setResetTokenExpiry(resetTokenExpiry);
            userRepository.save(user);

            // Tạo response
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Email reset password đã được gửi. Vui lòng kiểm tra email của bạn.");
            
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("resetToken", resetToken);
            data.put("email", request.getEmail());
            
            response.put("data", data);

            logger.info("Forgot password request for email: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (UnauthorizedException e) {
            Map<String, Object> errorResponse = new LinkedHashMap<>();
            errorResponse.put("status", HttpStatus.NOT_FOUND.value());
            errorResponse.put("message", e.getMessage());
            errorResponse.put("error", "NOT_FOUND");
            
            logger.warn("Forgot password failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * POST /api/auth/reset-password
     * Reset password sử dụng reset token
     */
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, Object>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        try {
            // Kiểm tra password và confirmPassword có khớp không
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                Map<String, Object> errorResponse = new LinkedHashMap<>();
                errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
                errorResponse.put("message", "Password và Confirm Password không khớp");
                errorResponse.put("error", "VALIDATION_ERROR");
                
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            // Tìm user theo reset token
            User user = userRepository.findByResetToken(request.getToken())
                    .orElseThrow(() -> new UnauthorizedException("Reset token không hợp lệ"));

            // Kiểm tra reset token có hết hạn không
            if (user.getResetTokenExpiry() == null || LocalDateTime.now().isAfter(user.getResetTokenExpiry())) {
                throw new UnauthorizedException("Reset token đã hết hạn");
            }

            // Cập nhật password mới
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            user.setResetToken(null);
            user.setResetTokenExpiry(null);
            userRepository.save(user);

            // Tạo response
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Đặt lại password thành công. Vui lòng đăng nhập với password mới.");
            
            response.put("data", null);

            logger.info("Password reset successfully for user: {}", user.getUsername());
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (UnauthorizedException e) {
            Map<String, Object> errorResponse = new LinkedHashMap<>();
            errorResponse.put("status", HttpStatus.UNAUTHORIZED.value());
            errorResponse.put("message", e.getMessage());
            errorResponse.put("error", "UNAUTHORIZED");
            
            logger.warn("Password reset failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    /**
     * POST /api/auth/refresh
     * Refresh access token sử dụng refresh token
     */
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refreshToken(@RequestBody Map<String, String> request) {
        try {
            String refreshToken = request.get("refreshToken");
            
            if (refreshToken == null || !jwtUtil.isTokenValid(refreshToken)) {
                throw new UnauthorizedException("Refresh token không hợp lệ");
            }

            String username = jwtUtil.getUsernameFromToken(refreshToken);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UnauthorizedException("User không tồn tại"));

            String newAccessToken = jwtUtil.generateToken(user);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Refresh token thành công");
            
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("accessToken", newAccessToken);
            data.put("tokenType", "Bearer");
            
            response.put("data", data);

            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (UnauthorizedException e) {
            Map<String, Object> errorResponse = new LinkedHashMap<>();
            errorResponse.put("status", HttpStatus.UNAUTHORIZED.value());
            errorResponse.put("message", e.getMessage());
            errorResponse.put("error", "UNAUTHORIZED");
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    /**
     * Inner class để trả về user info sau khi login
     */
    public static class UserLoginResponse {
        private Long id;
        private String username;
        private String email;
        private String fullName;
        private String role;

        public UserLoginResponse(Long id, String username, String email, String fullName, String role) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.fullName = fullName;
            this.role = role;
        }

        // Getters
        public Long getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public String getFullName() {
            return fullName;
        }

        public String getRole() {
            return role;
        }
    }
}
