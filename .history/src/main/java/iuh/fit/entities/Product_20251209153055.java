package iuh.fit.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200, nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "discount_price")
    private Double discountPrice;

    @Column(name = "discount")
    private Integer discount; // %

    @Column(name = "stock")
    private Integer stock;

    @Column(length = 2000, name = "image_names", columnDefinition = "TEXT")
    private String imageNames; // JSON array format: ["img1.jpg", "img2.png"]

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    // Quan hệ Nhiều sản phẩm thuộc 1 danh mục
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @ToString.Exclude
    private Category category;
}
