package elefant.clone.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Data
@Entity
@Table(name = "transaction")
public class Transaction extends BaseEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
        @GenericGenerator(name = "native", strategy = "native")
        private Long id;

        @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, targetEntity = Person.class)
        private Person person;

        @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL)
        private List<TransactionPairing> transactionPairings;

        private float totalPrice;

        public Person getPerson() {
                return person;
        }


        public void setPerson(Person person) {
                this.person = person;
        }

        public void setTotalPrice(float totalPrice) {
                this.totalPrice = totalPrice;
        }

        public float getTotalPrice() {
                return totalPrice;
        }

        public void setTransactionPairings(List<TransactionPairing> transactionPairings) {
                this.transactionPairings = transactionPairings;
        }

        public List<TransactionPairing> getTransactionPairings() {
                return transactionPairings;
        }
}