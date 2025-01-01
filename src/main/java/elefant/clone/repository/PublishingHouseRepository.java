package elefant.clone.repository;

import elefant.clone.model.Author;
import elefant.clone.model.Person;
import elefant.clone.model.PublishingHouse;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PublishingHouseRepository  extends CrudRepository<PublishingHouse, Integer> {
    List<PublishingHouse> findByName(String name);

}
