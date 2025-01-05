package elefant.clone.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import elefant.clone.model.BaseEntity;

@Data
@Entity
@Table(name="category_pairing")
public class CategoryPairing extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    private int id;

    @JsonBackReference("product-category-pairings")

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false)
    private Product product; // Relationship to the Book entity

    @JsonBackReference

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private Category category; // Relationship to the Author entity

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setId(int id) { this.id = id; }

    public int getId() { return id; }

    @Override
    public String toString() {
        return "CategoryPairing{id=" + getId() + ", category=" + (category != null ? category.getCategoryName() : "null") + "}";
    }
}
