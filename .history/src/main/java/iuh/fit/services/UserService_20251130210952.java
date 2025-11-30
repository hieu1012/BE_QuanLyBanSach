package iuh.fit.services;

import iuh.fit.dtos.user.CreateUserRequest;
import iuh.fit.dtos.user.UpdateUserRequest;
import iuh.fit.dtos.user.UserDTO;
import iuh.fit.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    /**
     * Lấy danh sách tất cả user (dựa trên quyền của currentUser)
     */
    List<UserDTO> findAll(User currentUser);

    /**
     * Lấy danh sách user có phân trang
     */
    Page<UserDTO> findAllWithPaging(User currentUser, Pageable pageable);

    /**
     * Tìm user theo ID
     */
    UserDTO findById(Long id, User currentUser);

    /**
     * Tạo user mới
     */
    UserDTO save(CreateUserRequest request, User currentUser);

    /**
     * Cập nhật thông tin user
     */
    UserDTO update(Long id, UpdateUserRequest request, User currentUser);

    /**
     * Xóa user
     */
    boolean delete(Long id, User currentUser);

    /**
     * Tìm kiếm user
     */
    List<UserDTO> search(String keyword, User currentUser);

    /**
     * Tìm kiếm user với phân trang
     */
    Page<UserDTO> searchWithPaging(String keyword, User currentUser, Pageable pageable);

    /**
     * Tìm user theo username (để authentication)
     */
    User findByUsername(String username);

    /**
     * Đổi password
     */
    boolean changePassword(Long id, String oldPassword, String newPassword, User currentUser);

    /**
     * Tìm kiếm user theo role
     */
    Page<UserDTO> findByRole(String role, User currentUser, Pageable pageable);

    /**
     * Tìm kiếm user theo isActive
     */
    Page<UserDTO> findByIsActive(Boolean isActive, User currentUser, Pageable pageable);

    /**
     * Tìm kiếm user theo keyword và role
     */
    Page<UserDTO> searchByKeywordAndRole(String keyword, String role, User currentUser, Pageable pageable);

    /**
     * Tìm kiếm user theo keyword và isActive
     */
    Page<UserDTO> searchByKeywordAndIsActive(String keyword, Boolean isActive, User currentUser, Pageable pageable);

}