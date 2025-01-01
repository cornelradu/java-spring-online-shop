package elefant.clone.controller;

import elefant.clone.model.Person;
import elefant.clone.model.Product;
import elefant.clone.model.PurchaseCart;
import elefant.clone.model.PurchaseCartProductPairing;
import elefant.clone.repository.ProductRepository;
import elefant.clone.repository.PurchaseCartProductPairingRepository;
import elefant.clone.repository.PurchaseCartRepository;
import elefant.clone.repository.PersonRepository;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@Slf4j
public class PurchaseCartController {

    @Autowired
    PurchaseCartRepository purchaseCartRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    PurchaseCartProductPairingRepository purchaseCartProductPairingRepository;

    @GetMapping("/cart/remove/{productId}")
    public ResponseEntity removeCart(@PathVariable("productId") String productId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = personRepository.readByName(auth.getName());
        if (person == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");

        Optional<PurchaseCart> purchaseCart = purchaseCartRepository.findByPersonId(person.getId());
        if(!purchaseCart.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");

        List<PurchaseCartProductPairing> copy = new ArrayList<>(purchaseCart.get().getProductPairings());

        purchaseCart.get().getProductPairings().removeIf(p -> p.getProduct().getId() == Integer.parseInt(productId));
        purchaseCartRepository.save(purchaseCart.get());
        for(var pairing : copy){
            if(pairing.getProduct().getId() == Integer.parseInt(productId)){
                purchaseCartProductPairingRepository.delete(pairing);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("Success");

    }

    @GetMapping("/cart/update/{productId}")
    public ResponseEntity updateCart(@PathVariable("productId") String productId, @PathParam("quantityChange") int quantityChange) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = personRepository.readByName(auth.getName());
        if(person == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");


        if(quantityChange < 0){
            for(var i = 0; i < Math.abs(quantityChange); i++){
                Optional<PurchaseCart> purchaseCart = purchaseCartRepository.findByPersonId(person.getId());
                if(!purchaseCart.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");

                Optional<PurchaseCartProductPairing> pairing = purchaseCart.get().getProductPairings().stream().filter(p -> p.getProduct().getId() == Integer.parseInt(productId)).findFirst();
                if(!pairing.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");


                purchaseCart.get().getProductPairings().remove(pairing.get());
                purchaseCartRepository.save(purchaseCart.get());
                purchaseCartProductPairingRepository.delete(pairing.get());
            }

        } else {
            for(var i = 0; i < quantityChange; i++){
                Optional<PurchaseCart> purchaseCart = purchaseCartRepository.findByPersonId(person.getId());
                if(!purchaseCart.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");


                PurchaseCartProductPairing newPairing = new PurchaseCartProductPairing();
                Optional<Product> p = productRepository.findById(Integer.parseInt(productId));
                newPairing.setProduct(p.get());
                newPairing.setPurchaseCart(purchaseCart.get());
                newPairing.setCreatedBy(1);
                newPairing.setUpdatedBy(1);
                purchaseCartProductPairingRepository.save(newPairing);

            }
        }

        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

    @GetMapping("/cart")
    public String displayCart(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = personRepository.readByName(auth.getName());
        if(person == null) {
            return  "redirect:/login";
        }
        float subtotal = 0;
        HashMap<Product, Integer> cart = new HashMap<>();

        Optional<PurchaseCart> purchaseCart = purchaseCartRepository.findByPersonId(person.getId());
        if(!purchaseCart.isPresent()) return "redirect:/home";
        else {
            purchaseCart.get().getProductPairings().sort((p1, p2) -> (int)(p1.getProduct().getId() - p2.getProduct().getId()));
            purchaseCart.get().getProductPairings().forEach(pairing -> {
                if(cart.containsKey(pairing.getProduct())){
                    cart.put(pairing.getProduct(), cart.get(pairing.getProduct()) + 1);
                } else{
                    cart.put(pairing.getProduct(), 1);
                }


            });
        }
        model.addAttribute("cart", cart);

        for(var entry : cart.entrySet()) {
            subtotal += entry.getKey().getPrice() * entry.getValue();
        }

        model.addAttribute("subtotal", subtotal);
        model.addAttribute("total", subtotal + 15.0);

        model.addAttribute("hide_image", true);
        model.addAttribute("person", person);
        return "cart.html";
    }

    @GetMapping("/cartCount")
    public ResponseEntity cartCount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = personRepository.readByName(auth.getName());
        if(person == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");

        Optional<PurchaseCart> purchaseCart = purchaseCartRepository.findByPersonId(person.getId());
        if(purchaseCart.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(purchaseCart.get().getProductPairings().size());
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(0);
        }
    }

    @GetMapping("/addToCart/{productId}")
    public ResponseEntity addToCart(@PathVariable("productId") String productId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = personRepository.readByName(auth.getName());
        if(person == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");

        PurchaseCart cart;
        Optional<PurchaseCart> purchaseCart = purchaseCartRepository.findByPersonId(person.getId());
        if(!purchaseCart.isPresent()){
            cart = new PurchaseCart();
            cart.setPerson(person);
            cart.setCreatedBy(1);
            cart.setUpdatedBy(1);
            purchaseCartRepository.save(cart);
        } else {
            cart = purchaseCart.get();
        }

        PurchaseCartProductPairing newPairing = new PurchaseCartProductPairing();
        Optional<Product> p = productRepository.findById(Integer.parseInt(productId));
        newPairing.setProduct(p.get());
        newPairing.setPurchaseCart(cart);
        newPairing.setCreatedBy(1);
        newPairing.setUpdatedBy(1);
        purchaseCartProductPairingRepository.save(newPairing);



        return ResponseEntity.status(HttpStatus.CREATED).body("Added to cart");
    }
}
