package elefant.clone.controller;

import elefant.clone.model.Vote;
import  elefant.clone.model.Product;
import elefant.clone.service.PersonService;
import elefant.clone.service.ProductService;
import elefant.clone.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class VoteController {

    @Autowired
    private VoteService voteService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PersonService personService;

    @GetMapping("/vote/{productId}/{personId}/{rating}")
    public ResponseEntity<Map<String, Object>> submitVote(
            @PathVariable int productId,
            @PathVariable int personId,
            @PathVariable int rating) {
        try {
            Vote vote = voteService.findByProductIdPersonId(personId, productId);

            if(vote == null) {
                vote = new Vote();
                vote.setProduct(productService.findById(productId));
                vote.setPerson(personService.findById(personId));
                vote.setRating(rating);
                voteService.save(vote);
            } else {
                vote.setRating(rating);
                voteService.save(vote);
            }
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Vote recorded successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Error recording vote"
            ));
        }
    }

    @GetMapping("/rating/{productId}")
    public ResponseEntity<Map<String, Object>> getRating(
            @PathVariable int productId
    ){
        Product product = productService.findById(productId);

        return ResponseEntity.ok(Map.of(
                "rating", product.getAverageRating()
        ));
    }

}
