package elefant.clone.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Data
@Entity
@Table(name="purchase_cart")
public class PurchaseCart  extends BaseEntity{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST, targetEntity = Person.class)
    private Person person;

    @OneToMany(mappedBy = "purchaseCart", cascade = CascadeType.ALL)
    private List<PurchaseCartProductPairing> productPairings;

    public List<PurchaseCartProductPairing> getProductPairings() {
        return productPairings;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }

    public float getTotalPrice() {
        float totalPrice = 0;
        for(PurchaseCartProductPairing pairing : productPairings) {
            totalPrice += pairing.getProduct().getPrice();
        }
        return totalPrice;
    }
}
