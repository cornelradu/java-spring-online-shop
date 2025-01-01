package elefant.clone.repository;

import elefant.clone.model.Person;
import org.springframework.data.repository.CrudRepository;
import elefant.clone.model.Transaction;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
    List<Transaction> findByPersonOrderByCreatedAtDesc(Person person);

}
