package elefant.clone.controller;

import elefant.clone.model.Category;
import elefant.clone.model.Person;
import elefant.clone.model.Product;
import elefant.clone.repository.CategoryRepository;
import elefant.clone.repository.PersonRepository;
import elefant.clone.service.ProductService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CategoriesController {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductService productService;

    @Autowired
    PersonRepository personRepository;


    @RequestMapping(value={ "categories"})
    public String displayHomePage(Model model, @PathParam("category_id") String category_id, @RequestParam(required = false) String categoryId,
                                  @PathParam("price") String price, @RequestParam(required = false) String priceId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = personRepository.readByName(auth.getName());

        List<Category> categoryList = categoryRepository.findAll();
        model.addAttribute("categories", categoryList);

        List<String> prices = List.of("0-100", "101-200", "201-300", ">300");

        model.addAttribute("prices", prices);

        if(categoryId != null && categoryId.equals("on")){
            model.addAttribute("categoryId", "on");
        } else {
            model.addAttribute("categoryId", "off");
        }

        List<Product> products = new ArrayList<>();
        if(category_id != null && categoryId != null && categoryId.equals("on")) {
            model.addAttribute("category_id", Integer.parseInt(category_id));
            products = productService.findByCategoryId(Integer.parseInt(category_id));
        } else {
            products = productService.findAll();
        }

        if(priceId != null && priceId.equals("on")){
            model.addAttribute("priceId", "on");
        } else {
            model.addAttribute("priceId", "off");
        }

        if(price != null && priceId != null && priceId.equals("on")) {
            model.addAttribute("price", price);
            if(price.contains("-")) {
                products = products.stream().filter(product -> product.getPrice() >= Integer.parseInt(price.split("-")[0]) && product.getPrice() <= Integer.parseInt(price.split("-")[1])).toList();
            } else if(price.contains(">")){
                products = products.stream().filter(product -> product.getPrice() > Integer.parseInt(price.split(">")[1])).toList();
            }
        }

        model.addAttribute("hide_image", true);
        model.addAttribute("length_products", products.size());
        model.addAttribute("products", products);
        model.addAttribute("person", person);

        return "categories.html";
    }
}
