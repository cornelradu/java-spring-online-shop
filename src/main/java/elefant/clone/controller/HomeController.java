package elefant.clone.controller;

import elefant.clone.model.Person;
import elefant.clone.model.ProductDTO;
import elefant.clone.repository.PersonRepository;
import elefant.clone.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import elefant.clone.util.CookieUtil;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    ProductService productService;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    private  CookieUtil cookieUtil;



    @RequestMapping(value={"", "/", "home"})
    public String displayHomePage(Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = personRepository.readByName(auth.getName());
        model.addAttribute("person", person);
        model.addAttribute("randomProducts", productService.getRandomProducts());
        List<Integer> productIds = cookieUtil.getRecentlyViewedProducts(request);
        List<elefant.clone.model.ProductDTO> products = productService.getProductsByIds(productIds);

        model.addAttribute("recentlyViewedProducts", products);

        return "home.html";
    }

}

