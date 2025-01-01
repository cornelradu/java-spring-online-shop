package elefant.clone.repository;

import elefant.clone.model.Author;
import elefant.clone.model.PurchaseCart;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PurchaseCartRepository extends CrudRepository<PurchaseCart, Integer> {
    Optional<PurchaseCart> findByPersonId(int personId);

}
