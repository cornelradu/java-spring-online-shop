package elefant.clone.service;

import elefant.clone.model.Product;
import elefant.clone.model.ProductDTO;
import elefant.clone.model.Vote;
import elefant.clone.repository.ProductRepository;
import elefant.clone.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;

    public void save(Vote vote) {
        voteRepository.save(vote);
    }

    public Vote findById(int id) {
        return this.voteRepository.findById(id).get();
    }

    public Vote findByProductIdPersonId(int personId, int productId) {
        return this.voteRepository.findByProductIdPersonId(productId, personId);
    }
}
