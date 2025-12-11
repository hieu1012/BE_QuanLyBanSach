package iuh.fit.services.impl;

import iuh.fit.dtos.user.CreateUserRequest;
import iuh.fit.dtos.user.UpdateUserRequest;
import iuh.fit.dtos.user.UserDTO;
import iuh.fit.entities.User;
import iuh.fit.entities.enums.Role;
import iuh.fit.exceptions.ForbiddenException;
import iuh.fit.exceptions.ItemNotFoundException;
import iuh.fit.exceptions.ValidationException;
import iuh.fit.repositories.UserRepository;
import iuh.fit.services.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           ModelMapper modelMapper,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    private UserDTO toDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    private void validateUser(User user) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        if (!violations.isEmpty()) {
            Map<String, Object> errors = new LinkedHashMap<>();
            violations.forEach(v -> errors.put(v.getPropertyPath().toString(), v.getMessage()));
            throw new ValidationException("Validation failed for user", errors);
        }
    }

    @Override
    public List<UserDTO> findAll(User currentUser) {
        List<User> users;

        if (currentUser.getRole() == Role.MASTER) {
            users = userRepository.findAll();
        } else if (currentUser.getRole() == Role.ADMIN) {
            // ADMIN không được xem MASTER
            users = userRepository.findByRoleNot(Role.MASTER);
        } else {
            throw new ForbiddenException("Bạn không có quyền xem danh sách user");
        }

        return users.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserDTO> findAllWithPaging(User currentUser, Pageable pageable) {
        Page<User> users;

        if (currentUser.getRole() == Role.MASTER) {
            users = userRepository.findAll(pageable);
        } else if (currentUser.getRole() == Role.ADMIN) {
            users = userRepository.findByRoleNot(Role.MASTER, pageable);
        } else {
            throw new ForbiddenException("Bạn không có quyền xem danh sách user");
        }

        return users.map(this::toDTO);
    }

    @Override
    public UserDTO findById(Long id, User currentUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("User not found with id: " + id));

        // ADMIN không được xem thông tin MASTER
        if (currentUser.getRole() == Role.ADMIN && user.getRole() == Role.MASTER) {
            throw new ForbiddenException("Bạn không có quyền xem thông tin này");
        }

        // USER chỉ được xem thông tin của chính mình
        if (currentUser.getRole() == Role.USER && !user.getId().equals(currentUser.getId())) {
            throw new ForbiddenException("Bạn chỉ có thể xem thông tin của chính mình");
        }

        return toDTO(user);
    }

    @Transactional
    @Override
    public UserDTO save(CreateUserRequest request, User currentUser) {
        // 1. CẤM TẠO MASTER QUA API
        if (request.getRole() == Role.MASTER) {
            throw new ForbiddenException("Không được phép tạo tài khoản MASTER qua API!");
        }

        // 2. Kiểm tra quyền tạo user (ADMIN chỉ tạo được USER/ADMIN, MASTER tạo được ADMIN)
        validateCreatePermission(request.getRole(), currentUser);

        // 3. Kiểm tra username/email trùng
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username đã tồn tại");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }

        // 4. Map dữ liệu
        User user = modelMapper.map(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // 5. Validate entity (nếu còn dùng)
        validateUser(user);

        User savedUser = userRepository.save(user);
        return toDTO(savedUser);
    }

    @Transactional
    @Override
    public UserDTO update(Long id, UpdateUserRequest request, User currentUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("User not found with id: " + id));

        // Kiểm tra quyền cập nhật
        validateUpdatePermission(user, currentUser);

        // Cập nhật thông tin
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Email đã tồn tại");
            }
            user.setEmail(request.getEmail());
        }

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }
        if (request.getIsActive() != null) {
            user.setIsActive(request.getIsActive());
        }

        User updatedUser = userRepository.save(user);
        return toDTO(updatedUser);
    }

    @Transactional
    @Override
    public boolean delete(Long id, User currentUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("User not found with id: " + id));

        // Kiểm tra quyền xóa
        validateDeletePermission(user, currentUser);

        userRepository.deleteById(id);
        return true;
    }

    @Transactional
    @Override
    public boolean deleteMultiple(List<Long> ids, User currentUser) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        try {
            for (Long id : ids) {
                User user = userRepository.findById(id)
                        .orElseThrow(() -> new ItemNotFoundException("User not found with id: " + id));
                validateDeletePermission(user, currentUser);
            }
            userRepository.deleteAllById(ids);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<UserDTO> search(String keyword, User currentUser) {
        if (keyword == null || keyword.isBlank()) {
            return findAll(currentUser);
        }

        List<User> users;
        if (currentUser.getRole() == Role.MASTER) {
            users = userRepository.search(keyword);
        } else if (currentUser.getRole() == Role.ADMIN) {
            users = userRepository.searchExcludingRole(keyword, Role.MASTER);
        } else {
            throw new ForbiddenException("Bạn không có quyền tìm kiếm user");
        }

        return users.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserDTO> searchWithPaging(String keyword, User currentUser, Pageable pageable) {
        Page<User> users;

        if (currentUser.getRole() == Role.MASTER) {
            users = userRepository.searchWithPaging(keyword, pageable);
        } else if (currentUser.getRole() == Role.ADMIN) {
            users = userRepository.searchWithPagingExcludingRole(keyword, Role.MASTER, pageable);
        } else {
            throw new ForbiddenException("Bạn không có quyền tìm kiếm user");
        }

        return users.map(this::toDTO);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ItemNotFoundException("Không tìm thấy user với username: " + username));
    }

    @Transactional
    @Override
    public boolean changePassword(Long id, String oldPassword, String newPassword, User currentUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("User not found with id: " + id));

        // Nếu oldPassword không null (user tự đổi password)
        // Kiểm tra user chỉ được đổi password của chính mình
        if (oldPassword != null && !user.getId().equals(currentUser.getId())) {
            throw new ForbiddenException("Bạn chỉ có thể đổi password của chính mình");
        }

        // Nếu oldPassword không null, phải kiểm tra old password
        if (oldPassword != null) {
            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                throw new IllegalArgumentException("Mật khẩu cũ không đúng");
            }
        }
        // Nếu oldPassword là null, chỉ ADMIN/MASTER mới được phép (đã kiểm tra ở controller)

        // Validate new password
        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("Password mới phải có ít nhất 6 ký tự");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }

    /**
     * Kiểm tra quyền tạo user
     */
    private void validateCreatePermission(Role roleToCreate, User currentUser) {
        if (currentUser.getRole() == Role.USER) {
            throw new ForbiddenException("USER không có quyền tạo tài khoản");
        }

        // ADMIN không thể tạo MASTER hoặc ADMIN khác
        if (currentUser.getRole() == Role.ADMIN &&
                (roleToCreate == Role.MASTER || roleToCreate == Role.ADMIN)) {
            throw new ForbiddenException("ADMIN không thể tạo tài khoản MASTER hoặc ADMIN");
        }
    }

    /**
     * Kiểm tra quyền cập nhật
     */
    private void validateUpdatePermission(User userToUpdate, User currentUser) {
        // Cho phép tự cập nhật thông tin của chính mình
        if (userToUpdate.getId().equals(currentUser.getId())) {
            return;
        }

        // ADMIN không thể cập nhật MASTER hoặc ADMIN khác
        if (currentUser.getRole() == Role.ADMIN &&
                (userToUpdate.getRole() == Role.MASTER || userToUpdate.getRole() == Role.ADMIN)) {
            throw new ForbiddenException("ADMIN không thể cập nhật tài khoản MASTER hoặc ADMIN khác");
        }

        // USER chỉ có thể cập nhật chính mình
        if (currentUser.getRole() == Role.USER) {
            throw new ForbiddenException("Bạn chỉ có thể cập nhật thông tin của chính mình");
        }
    }

    /**
     * Kiểm tra quyền xóa
     */
    private void validateDeletePermission(User userToDelete, User currentUser) {
        if (currentUser.getRole() == Role.USER) {
            throw new ForbiddenException("USER không có quyền xóa tài khoản");
        }

        // Không thể tự xóa chính mình
        if (userToDelete.getId().equals(currentUser.getId())) {
            throw new ForbiddenException("Không thể xóa chính mình");
        }

        // ADMIN không thể xóa MASTER hoặc ADMIN khác
        if (currentUser.getRole() == Role.ADMIN &&
                (userToDelete.getRole() == Role.MASTER || userToDelete.getRole() == Role.ADMIN)) {
            throw new ForbiddenException("ADMIN không thể xóa tài khoản MASTER hoặc ADMIN");
        }
    }

    @Override
    public Page<UserDTO> findByRole(String role, User currentUser, Pageable pageable) {
        Role roleEnum = Role.valueOf(role.toUpperCase());
        
        if (currentUser.getRole() == Role.ADMIN && roleEnum == Role.MASTER) {
            throw new ForbiddenException("ADMIN không có quyền xem MASTER");
        }

        if (currentUser.getRole() != Role.MASTER && currentUser.getRole() != Role.ADMIN) {
            throw new ForbiddenException("Bạn không có quyền tìm kiếm theo role");
        }

        return userRepository.findByRole(roleEnum, pageable).map(this::toDTO);
    }

    @Override
    public Page<UserDTO> findByIsActive(Boolean isActive, User currentUser, Pageable pageable) {
        if (currentUser.getRole() != Role.MASTER && currentUser.getRole() != Role.ADMIN) {
            throw new ForbiddenException("Bạn không có quyền tìm kiếm theo trạng thái hoạt động");
        }

        Page<User> users = userRepository.findByIsActive(isActive, pageable);
        return users.map(this::toDTO);
    }

    @Override
    public Page<UserDTO> searchByKeywordAndRole(String keyword, String role, User currentUser, Pageable pageable) {
        Role roleEnum = Role.valueOf(role.toUpperCase());
        
        if (currentUser.getRole() == Role.ADMIN && roleEnum == Role.MASTER) {
            throw new ForbiddenException("ADMIN không có quyền xem MASTER");
        }

        if (currentUser.getRole() != Role.MASTER && currentUser.getRole() != Role.ADMIN) {
            throw new ForbiddenException("Bạn không có quyền tìm kiếm");
        }

        return userRepository.searchByKeywordAndRole(keyword, roleEnum, pageable).map(this::toDTO);
    }

    @Override
    public Page<UserDTO> searchByKeywordAndIsActive(String keyword, Boolean isActive, User currentUser, Pageable pageable) {
        if (currentUser.getRole() != Role.MASTER && currentUser.getRole() != Role.ADMIN) {
            throw new ForbiddenException("Bạn không có quyền tìm kiếm");
        }

        Page<User> users = userRepository.searchByKeywordAndIsActive(keyword, isActive, pageable);
        return users.map(this::toDTO);
    }
}