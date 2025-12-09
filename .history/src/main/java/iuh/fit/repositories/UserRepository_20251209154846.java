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

    @Query(value = "SELECT u FROM User u ORDER BY u.id DESC")
    Page<User> findAll(Pageable pageable);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByResetToken(String resetToken);

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
    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')) " +
            "OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :fullName, '%')) " +
            "ORDER BY u.id DESC")
    Page<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrFullNameContainingIgnoreCase(
            @Param("username") String username, @Param("email") String email, @Param("fullName") String fullName, Pageable pageable);

    // Tìm kiếm với phân trang (excludes MASTER)
    @Query("SELECT u FROM User u WHERE u.role <> :excludeRole AND (" +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "ORDER BY u.id DESC")
    Page<User> searchWithPagingExcludingRole(@Param("keyword") String keyword, @Param("excludeRole") Role excludeRole, Pageable pageable);

    // Tìm kiếm với phân trang (tất cả)
    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "ORDER BY u.id DESC")
    Page<User> searchWithPaging(@Param("keyword") String keyword, Pageable pageable);

    boolean existsByRole(Role role);

    // Tìm kiếm theo role
    Page<User> findByRole(Role role, Pageable pageable);

    // Tìm kiếm theo isActive
    Page<User> findByIsActive(Boolean isActive, Pageable pageable);

    // Tìm kiếm theo role và isActive
    Page<User> findByRoleAndIsActive(Role role, Boolean isActive, Pageable pageable);

    // Tìm kiếm theo keyword và role
    @Query("SELECT u FROM User u WHERE (" +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND u.role = :role")
    Page<User> searchByKeywordAndRole(@Param("keyword") String keyword, @Param("role") Role role, Pageable pageable);

    // Tìm kiếm theo keyword và isActive
    @Query("SELECT u FROM User u WHERE (" +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND u.isActive = :isActive")
    Page<User> searchByKeywordAndIsActive(@Param("keyword") String keyword, @Param("isActive") Boolean isActive, Pageable pageable);

    // Tìm kiếm theo role và isActive (excludes MASTER)
    @Query("SELECT u FROM User u WHERE u.role <> :excludeRole AND u.role = :role AND u.isActive = :isActive")
    Page<User> findByRoleAndIsActiveExcludingRole(@Param("role") Role role, @Param("isActive") Boolean isActive, @Param("excludeRole") Role excludeRole, Pageable pageable);

}