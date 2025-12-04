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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
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
    public ResponseEntity<Map<String, Object>> getAllUsers() {

        User currentUser = getCurrentUser();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", userService.findAll(currentUser));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * GET /api/users/page
     * Lấy danh sách user có phân trang và tìm kiếm (hỗ trợ keyword, role, isActive)
     */
    @GetMapping("/page")
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<Page<UserDTO>> getUsersWithPage(
            @ParameterObject Pageable pageable,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Boolean isActive) {

        User currentUser = getCurrentUser();

        Page<UserDTO> response;
        boolean hasKeyword = keyword != null && !keyword.isEmpty();
        boolean hasRole = role != null && !role.isEmpty();
        boolean hasIsActive = isActive != null;

        // Kết hợp keyword + role + isActive
        if (hasKeyword && hasRole && hasIsActive) {
            response = userService.searchByKeywordAndRole(keyword, role, currentUser, pageable);
        }
        // Chỉ keyword + role
        else if (hasKeyword && hasRole) {
            response = userService.searchByKeywordAndRole(keyword, role, currentUser, pageable);
        }
        // Chỉ keyword + isActive
        else if (hasKeyword && hasIsActive) {
            response = userService.searchByKeywordAndIsActive(keyword, isActive, currentUser, pageable);
        }
        // Chỉ role + isActive
        else if (hasRole && hasIsActive) {
            response = userService.findByRole(role, currentUser, pageable);
        }
        // Chỉ keyword
        else if (hasKeyword) {
            response = userService.searchWithPaging(keyword, currentUser, pageable);
        }
        // Chỉ role
        else if (hasRole) {
            response = userService.findByRole(role, currentUser, pageable);
        }
        // Chỉ isActive
        else if (hasIsActive) {
            response = userService.findByIsActive(isActive, currentUser, pageable);
        }
        // Không có filter nào
        else {
            response = userService.findAllWithPaging(currentUser, pageable);
        }

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
     * GET /api/users/{id}
     * Lấy chi tiết user theo ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        User currentUser = getCurrentUser();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", userService.findById(id, currentUser));
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

    /**
     * POST /api/users
     * Tạo user mới
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> createUser(@Valid @RequestBody CreateUserRequest request) {
        User currentUser = getCurrentUser();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.CREATED.value());
        response.put("data", userService.save(request, currentUser));
        response.put("message", "Tạo user thành công");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/users/{id}
     * Cập nhật user
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        User currentUser = getCurrentUser();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", userService.update(id, request, currentUser));
        response.put("message", "Cập nhật user thành công");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * DELETE /api/users/{id} hoặc DELETE /api/users?ids=1&ids=2
     * Xóa 1 user hoặc nhiều users
     */
    @DeleteMapping(value = {"/{id}", ""})
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> deleteUser(
            @PathVariable(required = false) Long id,
            @RequestParam(required = false) Long deleteId,
            @RequestParam(required = false) List<Long> ids) {
        
        User currentUser = getCurrentUser();
        Map<String, Object> response = new LinkedHashMap<>();
        
        // Nếu có ids (query param) → xóa nhiều (ưu tiên nếu có)
        if (ids != null && !ids.isEmpty()) {
            userService.deleteMultiple(ids, currentUser);
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Xóa " + ids.size() + " user thành công");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        
        // Nếu có id (path param hoặc query param) → xóa 1
        Long deleteIdFinal = id != null ? id : (deleteId != null ? deleteId : null);
        if (deleteIdFinal != null && deleteIdFinal > 0) {
            userService.delete(deleteIdFinal, currentUser);
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Xóa user thành công");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        
        // Nếu không có ID nào
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", "Vui lòng cung cấp id hoặc danh sách ids");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}