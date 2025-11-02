package iuh.fit.controllers;

import iuh.fit.dtos.user.CreateUserRequest;
import iuh.fit.dtos.user.UpdateUserRequest;
import iuh.fit.dtos.user.UserDTO;
import iuh.fit.entities.User;
import iuh.fit.entities.enums.Role;
import iuh.fit.services.UserService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class.getName());

    @Autowired
    private UserService userService;

    /**
     * Mock user với quyền MASTER để test
     */
    private User getMockCurrentUser() {
        User mockUser = new User();
        mockUser.setId(999L);
        mockUser.setUsername("test-master");
        mockUser.setEmail("test@master.com");
        mockUser.setRole(Role.MASTER);
        mockUser.setIsActive(true);
        mockUser.setFullName("Test Master User");
        return mockUser;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers(
            @RequestParam(required = false) String keyword) {

        User currentUser = getMockCurrentUser();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());

        if (keyword == null || keyword.isEmpty()) {
            response.put("data", userService.findAll(currentUser));
        } else {
            response.put("data", userService.search(keyword, currentUser));
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        User currentUser = getMockCurrentUser();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", userService.findById(id, currentUser));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(
            @Valid @RequestBody CreateUserRequest request) {

        User currentUser = getMockCurrentUser();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.CREATED.value());
        response.put("data", userService.save(request, currentUser));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {

        User currentUser = getMockCurrentUser();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", userService.update(id, request, currentUser));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        User currentUser = getMockCurrentUser();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", userService.delete(id, currentUser));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/usersHasPage")
    public ResponseEntity<Page<UserDTO>> getUsersWithPage(
            @ParameterObject Pageable pageable) {

        User currentUser = getMockCurrentUser();

        Page<UserDTO> response = userService.findAllWithPaging(currentUser, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<Map<String, Object>> changePassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> passwords) {

        User currentUser = getMockCurrentUser();

        String oldPassword = passwords.get("oldPassword");
        String newPassword = passwords.get("newPassword");

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", userService.changePassword(id, oldPassword, newPassword, currentUser));
        response.put("message", "Đổi password thành công");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}