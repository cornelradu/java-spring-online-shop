package elefant.clone.controller;

import elefant.clone.model.*;
import elefant.clone.repository.*;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BuyController {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PurchaseCartRepository  purchaseCartRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionPairingRepository transactionPairingRepository;

    @Autowired
    private PurchaseCartProductPairingRepository purchaseCartProductPairingRepository;

    @RequestMapping(value={ "/buy"})
    public String displaySuccessPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = personRepository.readByName(auth.getName());

        PurchaseCart purchaseCart = purchaseCartRepository.findByPersonId(person.getId()).get();

        Transaction t = new Transaction();
        t.setPerson(person);
        t.setTotalPrice(purchaseCart.getTotalPrice());
        t.setCreatedBy(1);
        t.setUpdatedBy(1);
        transactionRepository.save(t);

        List<Long> productIds = new ArrayList<>();
        for(PurchaseCartProductPairing pairing : purchaseCart.getProductPairings()) {
            TransactionPairing tp = new TransactionPairing();
            tp.setProduct(pairing.getProduct());
            tp.setTransaction(t);
            tp.setCreatedBy(1);
            tp.setUpdatedBy(1);
            tp.setCreatedAt(LocalDateTime.now());
            transactionPairingRepository.save(tp);

            productIds.add(pairing.getProduct().getId());
        }

        for(var prodId : productIds) {
            List<PurchaseCartProductPairing> copy = new ArrayList<>(purchaseCart.getProductPairings());
            purchaseCart.getProductPairings().removeIf(p -> p.getProduct().getId() == prodId);
            purchaseCartRepository.save(purchaseCart);
            for (var pairing : copy) {
                if (pairing.getProduct().getId() == prodId) {
                    purchaseCartProductPairingRepository.delete(pairing);
                }
            }
        }


        model.addAttribute("person", person);
        model.addAttribute("hide_image", true);
        return "buy.html";
    }

}
