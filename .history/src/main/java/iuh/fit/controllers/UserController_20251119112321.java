package iuh.fit.controllers;

import iuh.fit.dtos.user.CreateUserRequest;
import iuh.fit.dtos.user.UpdateUserRequest;
import iuh.fit.dtos.user.UserDTO;
import iuh.fit.entities.User;
import iuh.fit.exceptions.ForbiddenException;
import iuh.fit.exceptions.ItemNotFoundException;
import iuh.fit.exceptions.UnauthorizedException;
import iuh.fit.repositories.UserRepository;
import iuh.fit.services.UserService;
import iuh.fit.entities.enums.Role;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springdoc.core.annotations.ParameterObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class.getName());

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private ModelMapper modelMapper;

    private UserDTO toDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    /**
     * Lấy user hiện tại từ Security Context
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Vui lòng đăng nhập");
        }

        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("User không tồn tại"));
    }

    /**
     * GET /api/users
     * Lấy danh sách tất cả user - yêu cầu MASTER hoặc ADMIN
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> getAllUsers(
            @RequestParam(required = false) String keyword) {

        User currentUser = getCurrentUser();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());

        if (keyword == null || keyword.isEmpty()) {
            response.put("data", userService.findAll(currentUser));
        } else {
            response.put("data", userService.search(keyword, currentUser));
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * GET /api/users/{id}
     * Lấy chi tiết user
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN', 'USER')")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        User currentUser = getCurrentUser();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", userService.findById(id, currentUser));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * POST /api/users
     * Tạo user mới - yêu cầu MASTER hoặc ADMIN
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> createUser(
            @Valid @RequestBody CreateUserRequest request) {

        User currentUser = getCurrentUser();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.CREATED.value());
        response.put("data", userService.save(request, currentUser));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/users/{id}
     * Cập nhật user
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN', 'USER')")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {

        User currentUser = getCurrentUser();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", userService.update(id, request, currentUser));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * DELETE /api/users/{id}
     * Xóa user - yêu cầu MASTER hoặc ADMIN
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        User currentUser = getCurrentUser();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", userService.delete(id, currentUser));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * GET /api/users/page
     * Lấy danh sách user có phân trang
     */
    @GetMapping("/page")
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<Page<UserDTO>> getUsersWithPage(
            @ParameterObject Pageable pageable) {

        User currentUser = getCurrentUser();

        Page<UserDTO> response = userService.findAllWithPaging(currentUser, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * PUT /api/users/{id}/change-password
     * Đổi mật khẩu
     */
    @PutMapping("/{id}/change-password")
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN', 'USER')")
    public ResponseEntity<Map<String, Object>> changePassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> passwords) {

        User currentUser = getCurrentUser();

        String oldPassword = passwords.get("oldPassword");
        String newPassword = passwords.get("newPassword");

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", userService.changePassword(id, oldPassword, newPassword, currentUser));
        response.put("message", "Đổi password thành công");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * GET /api/users/profile
     * Lấy thông tin user hiện tại
     */
    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN', 'USER')")
    public ResponseEntity<Map<String, Object>> getCurrentUserProfile() {
        User currentUser = getCurrentUser();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", toDTO(currentUser));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}