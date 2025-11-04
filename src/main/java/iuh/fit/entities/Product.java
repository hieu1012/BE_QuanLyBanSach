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

    private Double price;
    private Double discountPrice;

    private Integer discount; // %
    private Integer stock;

    @Column(length = 255)
    private String image;

    @Column(nullable = false)
    private Boolean isActive = true;

    // Quan hệ Nhiều sản phẩm thuộc 1 danh mục
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @ToString.Exclude
    private Category category;
}
