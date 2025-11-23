package iuh.fit.controllers;

import iuh.fit.dtos.user.LoginRequest;
import iuh.fit.dtos.user.RegisterRequest;
import iuh.fit.entities.User;
import iuh.fit.entities.enums.Role;
import iuh.fit.exceptions.UnauthorizedException;
import iuh.fit.exceptions.ValidationException;
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

import java.util.LinkedHashMap;
import java.util.Map;

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
