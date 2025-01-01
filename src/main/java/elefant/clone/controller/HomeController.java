package elefant.clone.controller;

import elefant.clone.model.Person;
import elefant.clone.repository.PersonRepository;
import elefant.clone.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @Autowired
    ProductService productService;

    @Autowired
    PersonRepository personRepository;

    @RequestMapping(value={"", "/", "home"})
    public String displayHomePage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = personRepository.readByName(auth.getName());
        model.addAttribute("person", person);
        model.addAttribute("randomProducts", productService.getRandomProducts());

        return "home.html";
    }

}

