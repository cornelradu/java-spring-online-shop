package elefant.clone.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Data
@Entity
@Table(name="purchase_cart_product_pairing")
public class PurchaseCartProductPairing extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    private int id;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "purchase_cart_id", referencedColumnName = "id", nullable = false)
    private PurchaseCart purchaseCart;

    public int getId() {
        return id;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setPurchaseCart(PurchaseCart purchaseCart) {
        this.purchaseCart = purchaseCart;
    }

    public PurchaseCart getPurchaseCart() {
        return purchaseCart;
    }

    public Product getProduct() {
        return product;
    }
}
