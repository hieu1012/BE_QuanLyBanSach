package iuh.fit.repositories;

import iuh.fit.entities.User;
import iuh.fit.entities.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findByRole(Role role);

    // Tìm tất cả user trừ role cụ thể (dùng để ADMIN không xem được MASTER)
    List<User> findByRoleNot(Role role);

    Page<User> findByRoleNot(Role role, Pageable pageable);

    // Tìm kiếm user theo username, email, fullName
    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<User> search(@Param("keyword") String keyword);

    @Query("SELECT u FROM User u WHERE u.role <> :excludeRole AND (" +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<User> searchExcludingRole(@Param("keyword") String keyword, @Param("excludeRole") Role excludeRole);

    // Phân trang
    Page<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrFullNameContainingIgnoreCase(
            String username, String email, String fullName, Pageable pageable);

    boolean existsByRole(Role role);
}