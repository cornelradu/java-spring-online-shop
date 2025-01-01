package elefant.clone.repository;

import elefant.clone.model.PurchaseCartProductPairing;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

public interface PurchaseCartProductPairingRepository extends CrudRepository<PurchaseCartProductPairing, Integer> {
    @Transactional
    void removePurchaseCartProductPairingById(int id);
}
