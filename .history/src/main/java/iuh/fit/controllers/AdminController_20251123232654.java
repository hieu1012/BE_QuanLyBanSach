package iuh.fit.controllers;

import iuh.fit.dtos.admin.AdminCreateUserRequest;
import iuh.fit.dtos.admin.AdminUpdateUserRequest;
import iuh.fit.dtos.user.UserDTO;
import iuh.fit.entities.User;
import iuh.fit.entities.enums.Role;
import iuh.fit.exceptions.ItemNotFoundException;
import iuh.fit.repositories.UserRepository;
import iuh.fit.services.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Admin Controller
 * Quản lý người dùng - Chỉ admin có quyền truy cập
 */
@RestController
@RequestMapping("/admin/users")
@PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

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
     * GET /api/admin/users
     * Lấy danh sách tất cả users
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<User> users = userRepository.findAll(pageable);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Lấy danh sách users thành công");

            Map<String, Object> data = new LinkedHashMap<>();
            data.put("content", users.getContent().stream().map(this::toDTO).collect(Collectors.toList()));
            data.put("currentPage", users.getNumber());
            data.put("totalItems", users.getTotalElements());
            data.put("totalPages", users.getTotalPages());

            response.put("data", data);

            logger.info("Admin retrieved all users");
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new LinkedHashMap<>();
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("message", "Lỗi khi lấy danh sách users");
            errorResponse.put("error", "INTERNAL_SERVER_ERROR");

            logger.error("Error retrieving users: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * GET /api/admin/users/{id}
     * Lấy thông tin chi tiết user theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new ItemNotFoundException("User không tồn tại"));

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Lấy thông tin user thành công");
            response.put("data", toDTO(user));

            logger.info("Admin retrieved user with id: {}", id);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (ItemNotFoundException e) {
            Map<String, Object> errorResponse = new LinkedHashMap<>();
            errorResponse.put("status", HttpStatus.NOT_FOUND.value());
            errorResponse.put("message", e.getMessage());
            errorResponse.put("error", "NOT_FOUND");

            logger.warn("User not found: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * POST /api/admin/users
     * Tạo user mới (Admin)
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@Valid @RequestBody AdminCreateUserRequest request) {
        try {
            // Kiểm tra username đã tồn tại chưa
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                Map<String, Object> errorResponse = new LinkedHashMap<>();
                errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
                errorResponse.put("message", "Username đã tồn tại");
                errorResponse.put("error", "VALIDATION_ERROR");

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            // Kiểm tra email đã tồn tại chưa
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                Map<String, Object> errorResponse = new LinkedHashMap<>();
                errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
                errorResponse.put("message", "Email đã tồn tại");
                errorResponse.put("error", "VALIDATION_ERROR");

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            // Validate role
            Role role;
            try {
                role = Role.valueOf(request.getRole().toUpperCase());
            } catch (IllegalArgumentException e) {
                Map<String, Object> errorResponse = new LinkedHashMap<>();
                errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
                errorResponse.put("message", "Role không hợp lệ. Các role hợp lệ: MASTER, ADMIN, USER");
                errorResponse.put("error", "VALIDATION_ERROR");

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            // Tạo user mới
            User newUser = new User();
            newUser.setUsername(request.getUsername());
            newUser.setEmail(request.getEmail());
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));
            newUser.setFullName(request.getFullName());
            newUser.setPhoneNumber(request.getPhoneNumber());
            newUser.setAddress(request.getAddress());
            newUser.setRole(role);
            newUser.setIsActive(true);

            User savedUser = userRepository.save(newUser);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("status", HttpStatus.CREATED.value());
            response.put("message", "Tạo user thành công");
            response.put("data", toDTO(savedUser));

            logger.info("Admin created user: {}", request.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new LinkedHashMap<>();
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("message", "Lỗi khi tạo user");
            errorResponse.put("error", "INTERNAL_SERVER_ERROR");

            logger.error("Error creating user: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * PUT /api/admin/users/{id}
     * Cập nhật thông tin user (Admin)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody AdminUpdateUserRequest request) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new ItemNotFoundException("User không tồn tại"));

            // Cập nhật thông tin
            if (request.getFullName() != null && !request.getFullName().isEmpty()) {
                user.setFullName(request.getFullName());
            }
            if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
                user.setPhoneNumber(request.getPhoneNumber());
            }
            if (request.getAddress() != null && !request.getAddress().isEmpty()) {
                user.setAddress(request.getAddress());
            }

            // Cập nhật email nếu có và khác email hiện tại
            if (request.getEmail() != null && !request.getEmail().isEmpty() && !request.getEmail().equals(user.getEmail())) {
                if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                    Map<String, Object> errorResponse = new LinkedHashMap<>();
                    errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
                    errorResponse.put("message", "Email đã được sử dụng");
                    errorResponse.put("error", "VALIDATION_ERROR");

                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
                }
                user.setEmail(request.getEmail());
            }

            // Cập nhật role nếu có
            if (request.getRole() != null && !request.getRole().isEmpty()) {
                try {
                    Role role = Role.valueOf(request.getRole().toUpperCase());
                    user.setRole(role);
                } catch (IllegalArgumentException e) {
                    Map<String, Object> errorResponse = new LinkedHashMap<>();
                    errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
                    errorResponse.put("message", "Role không hợp lệ. Các role hợp lệ: MASTER, ADMIN, USER");
                    errorResponse.put("error", "VALIDATION_ERROR");

                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
                }
            }

            User updatedUser = userRepository.save(user);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Cập nhật user thành công");
            response.put("data", toDTO(updatedUser));

            logger.info("Admin updated user: {}", id);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (ItemNotFoundException e) {
            Map<String, Object> errorResponse = new LinkedHashMap<>();
            errorResponse.put("status", HttpStatus.NOT_FOUND.value());
            errorResponse.put("message", e.getMessage());
            errorResponse.put("error", "NOT_FOUND");

            logger.warn("User not found: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new LinkedHashMap<>();
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("message", "Lỗi khi cập nhật user");
            errorResponse.put("error", "INTERNAL_SERVER_ERROR");

            logger.error("Error updating user: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * DELETE /api/admin/users/{id}
     * Xóa user (Admin)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new ItemNotFoundException("User không tồn tại"));

            // Không cho phép xóa user MASTER
            if (user.getRole() == Role.MASTER) {
                Map<String, Object> errorResponse = new LinkedHashMap<>();
                errorResponse.put("status", HttpStatus.FORBIDDEN.value());
                errorResponse.put("message", "Không thể xóa user MASTER");
                errorResponse.put("error", "FORBIDDEN");

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }

            userRepository.delete(user);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Xóa user thành công");
            response.put("data", null);

            logger.info("Admin deleted user: {}", id);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (ItemNotFoundException e) {
            Map<String, Object> errorResponse = new LinkedHashMap<>();
            errorResponse.put("status", HttpStatus.NOT_FOUND.value());
            errorResponse.put("message", e.getMessage());
            errorResponse.put("error", "NOT_FOUND");

            logger.warn("User not found: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new LinkedHashMap<>();
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("message", "Lỗi khi xóa user");
            errorResponse.put("error", "INTERNAL_SERVER_ERROR");

            logger.error("Error deleting user: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
