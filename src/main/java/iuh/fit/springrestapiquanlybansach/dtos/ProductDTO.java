package iuh.fit.springrestapiquanlybansach.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Column;

/**
 * DTO cho product theo cấu trúc bảng:
 * id, title, description, price, discount, discountPrice, image, isActive, stock, categoryId
 */
public class ProductDTO {

    private Long id;

    @NotNull(message = "Title must not be null")
    @NotEmpty(message = "Title must not be empty")
    private String title;

    private String description;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be >= 0")
    private Double price;

    // discount có thể null nếu không áp dụng
    @Min(value = 0, message = "Discount must be >= 0")
    private Double discount;

    @Column(name = "discount_price")
    private Double discountPrice; // (tùy tính toán từ price & discount)

    private String image;

    private Boolean isActive;

    @Min(value = 0, message = "Stock must be >= 0")
    private Integer stock;

    // Id của category (nếu bạn muốn mapping relation, có thể thay bằng CategoryDTO)
    private Long categoryId;

    public ProductDTO() {}

    // Getters / Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}