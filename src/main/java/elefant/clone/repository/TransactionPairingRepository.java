package elefant.clone.repository;

import elefant.clone.model.AuthorPairing;
import elefant.clone.model.TransactionPairing;
import org.springframework.data.repository.CrudRepository;

public interface TransactionPairingRepository extends CrudRepository<TransactionPairing, Integer> {

}
