package elefant.clone.controller;

import elefant.clone.model.Person;
import elefant.clone.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DetailsPageController {

    @Autowired
    ProductRepository productRepository;

    @RequestMapping(value={"/details/{product_id}"})
    public String displayHomePage(Model model, @PathVariable int product_id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("hide_image", true);
        productRepository.findById(product_id).ifPresent(product -> model.addAttribute("product", product));

        return "details.html";
    }
}
