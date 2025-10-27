package iuh.fit.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entity tương ứng với bảng `product` (theo hình bạn gửi).
 * Sử dụng trường categoryId (Long) thay vì mapping @ManyToOne để tránh phụ thuộc nếu bạn chưa có entity Category.
 * Nếu bạn đã có Category entity và muốn mapping quan hệ, mình có thể cập nhật lại thành @ManyToOne.
 */
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Double discount;

    @Column(name = "discount_price")
    private Double discountPrice;

    private String image;

    @Column(name = "is_active")
    private Boolean isActive;

    private Double price;

    private Integer stock;

    @Column(name = "category_id")
    private Long categoryId;

    public Product() {}

    // Constructor tiện dụng
    public Product(String title, String description, Double price, Double discount, Double discountPrice,
                   String image, Boolean isActive, Integer stock, Long categoryId) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.discountPrice = discountPrice;
        this.image = image;
        this.isActive = isActive;
        this.stock = stock;
        this.categoryId = categoryId;
    }

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    @Override
    public String toString() {
        return "Product [id=" + id +
                ", title=" + title +
                ", price=" + price +
                ", discount=" + discount +
                ", discountPrice=" + discountPrice +
                ", stock=" + stock +
                ", isActive=" + isActive +
                ", categoryId=" + categoryId + "]";
    }
}