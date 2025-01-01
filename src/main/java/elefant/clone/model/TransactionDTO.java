package elefant.clone.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TransactionDTO {
    private Long id;
    private LocalDateTime createdAt;
    private float totalPrice;
    private Person person;
    private List<TransactionPairingDTO> groupedItems;

    public TransactionDTO(Transaction transaction, List<TransactionPairingDTO> groupedItems) {
        this.id = transaction.getId();
        this.createdAt = transaction.getCreatedAt();
        this.totalPrice = transaction.getTotalPrice();
        this.person = transaction.getPerson();
        this.groupedItems = groupedItems;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public Person getPerson() {
        return person;
    }

    public List<TransactionPairingDTO> getGroupedItems() {
        return groupedItems;
    }
}
