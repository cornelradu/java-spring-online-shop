package elefant.clone.model;

import lombok.Data;

@Data
public class TransactionPairingDTO {
    private Product product;
    private int quantity;

    public TransactionPairingDTO(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}