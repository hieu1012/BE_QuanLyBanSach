package iuh.fit.dtos.user;

import jakarta.validation.constraints.Email;

/**
 * DTO cho việc cập nhật thông tin user - không bao gồm password và role
 */
public class UpdateUserRequest {

    @Email(message = "Email không hợp lệ")
    private String email;

    private String fullName;
    private String phoneNumber;
    private String address;
    private Boolean isActive;

    // Constructors
    public UpdateUserRequest() {}

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}