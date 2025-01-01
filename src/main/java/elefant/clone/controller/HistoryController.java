package elefant.clone.controller;

import elefant.clone.model.*;
import elefant.clone.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HistoryController {
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

    @RequestMapping(value={ "/history"})
    public String displayHistoryPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = personRepository.readByName(auth.getName());

        if(person == null) {
            return "redirect:/login";
        }

        List<Transaction> transactions = transactionRepository.findByPersonOrderByCreatedAtDesc(person);


        List<TransactionDTO> transactionDTOs = transactions.stream().map(transaction -> {
            Map<Product, Integer> groupedItems = new HashMap<>();

            // Group items by product
            transaction.getTransactionPairings().forEach(pairing -> {
                groupedItems.merge(pairing.getProduct(), 1, Integer::sum);
            });

            // Create DTO with the original transaction and grouped items
            return new TransactionDTO(
                    transaction,
                    groupedItems.entrySet().stream()
                            .map(entry -> new TransactionPairingDTO(entry.getKey(), entry.getValue()))
                            .collect(Collectors.toList())
            );
        }).collect(Collectors.toList());
        model.addAttribute("transactions", transactionDTOs);

        model.addAttribute("person", person);
        model.addAttribute("hide_image", true);
        return "history.html";
    }

}
