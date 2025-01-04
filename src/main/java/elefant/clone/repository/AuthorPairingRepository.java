package elefant.clone.repository;

import elefant.clone.model.AuthorPairing;
import elefant.clone.model.Category;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AuthorPairingRepository extends CrudRepository<AuthorPairing, Integer> {
    @Modifying
    @Query("DELETE FROM AuthorPairing ap WHERE ap.product.id = :productId")
    void deleteByProductId(@Param("productId") Long productId);
}
