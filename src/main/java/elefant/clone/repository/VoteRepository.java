package elefant.clone.repository;

import elefant.clone.model.AuthorPairing;
import elefant.clone.model.Vote;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface VoteRepository extends CrudRepository<Vote, Integer> {

    @Query(value = "SELECT v FROM Vote v WHERE v.product.id= :productId AND v.person.id = :personId")
    Vote findByProductIdPersonId(@Param("productId") int productId, @Param("personId") int personId);
}
