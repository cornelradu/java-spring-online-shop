package elefant.clone.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import elefant.clone.model.Category;
import elefant.clone.model.Person;
import elefant.clone.model.Product;
import elefant.clone.repository.CategoryRepository;
import elefant.clone.repository.PersonRepository;
import elefant.clone.service.ProductService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
public class CategoriesController {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductService productService;

    @Autowired
    PersonRepository personRepository;


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @RequestMapping(value={ "/categories/carte_copii"})
    public String displayCarteCopii(Model model) {
        List<Category> categories = categoryRepository.findByCategoryName("Carte copii");
        return "redirect:/categories?category_id=" + categories.get(0).getId() + "&price=&overrideCategoryId=on";
    }

    @RequestMapping(value={ "/categories/carte_straina"})
    public String displayCarteStraina(Model model) {
        List<Category> categories = categoryRepository.findByCategoryName("Carte straina");
        return "redirect:/categories?category_id=" + categories.get(0).getId() + "&price=&overrideCategoryId=on";
    }


    @RequestMapping(value={ "/categories/carte_straina_copii"})
    public String displayCarteStrainaCopii(Model model) {
        List<Category> categories = categoryRepository.findByCategoryName("Carte straina copii");
        return "redirect:/categories?category_id=" + categories.get(0).getId() + "&price=&overrideCategoryId=on";
    }

    @RequestMapping(value={ "/categories/jocuri"})
    public String displayJocuri(Model model) {
        List<Category> categories = categoryRepository.findByCategoryName("Jocuri");
        return "redirect:/categories?category_id=" + categories.get(0).getId() + "&price=&overrideCategoryId=on";
    }

    @RequestMapping(value={ "/categories/jocuri_de_societate"})
    public String displayJocuriDeSocietate(Model model) {
        List<Category> categories = categoryRepository.findByCategoryName("Jocuri de societate");
        return "redirect:/categories?category_id=" + categories.get(0).getId() + "&price=&overrideCategoryId=on";
    }

    @RequestMapping(value={ "/categories/lego"})
    public String displayLEGO(Model model) {
        List<Category> categories = categoryRepository.findByCategoryName("LEGO");
        return "redirect:/categories?category_id=" + categories.get(0).getId() + "&price=&overrideCategoryId=on";
    }

    @RequestMapping(value={ "/categories/parfumuri"})
    public String displayParfumuri(Model model) {
        List<Category> categories = categoryRepository.findByCategoryName("Parfumuri");
        return "redirect:/categories?category_id=" + categories.get(0).getId() + "&price=&overrideCategoryId=on";
    }


    @RequestMapping(value={ "/categories"})
    public String displayHomePage(Model model, @PathParam("category_id") String category_id, @RequestParam(required = false) String categoryId,
                                  @PathParam("price") String price, @RequestParam(required = false) String priceId, @PathParam("overrideCategoryId") String overrideCategoryId) {
        if(overrideCategoryId != null){
            categoryId = overrideCategoryId;
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = personRepository.readByName(auth.getName());

        List<Category> categoryList = categoryRepository.findAll();
        model.addAttribute("categories", categoryList);

        List<String> prices = List.of("0-100", "101-200", "201-300", ">300");

        model.addAttribute("prices", prices);

        List<Product> products = new ArrayList<>();

        String redisKey = "category_id=" + category_id + ",price=" + price + ",priceId=" + priceId + ",categoryId=" + categoryId;
        Object value = redisTemplate.opsForValue().get(redisKey);
        boolean foundRedis = false;
        if (value instanceof List) {
            products = (List<Product>) value;
            foundRedis = true;
        }

        if (categoryId != null && categoryId.equals("on")) {
            model.addAttribute("categoryId", "on");
        } else {
            model.addAttribute("categoryId", "off");
        }

        if (category_id != null && categoryId != null && categoryId.equals("on")) {
            model.addAttribute("category_id", Integer.parseInt(category_id));
        }

        if (!foundRedis) {
            if (category_id != null && categoryId != null && categoryId.equals("on")) {
                model.addAttribute("category_id", Integer.parseInt(category_id));
                products = productService.findByCategoryId(Integer.parseInt(category_id));
            } else {
                products = productService.findAll();
            }
        }

        if (priceId != null && priceId.equals("on")) {
            model.addAttribute("priceId", "on");
        } else {
            model.addAttribute("priceId", "off");
        }

        if (price != null && priceId != null && !priceId.equals("") && priceId.equals("on")) {
            model.addAttribute("price", price);
            if (!foundRedis) {
                if (price.contains("-")) {
                    products = products.stream().filter(product -> product.getPrice() >= Integer.parseInt(price.split("-")[0]) && product.getPrice() <= Integer.parseInt(price.split("-")[1])).toList();
                } else if (price.contains(">")) {
                    products = products.stream().filter(product -> product.getPrice() > Integer.parseInt(price.split(">")[1])).toList();
                }
            }
        }

        if (!foundRedis) {
            redisTemplate.opsForValue().set(redisKey, products);
            // Optional: Set expiration
            redisTemplate.expire(redisKey, 1, TimeUnit.HOURS);
        }

        if (foundRedis) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            List<Product> newProducts = new ArrayList<>();
            for (Object item : products) {
                if (item instanceof LinkedHashMap) {
                    // Convert LinkedHashMap to Product
                    Product product = objectMapper.convertValue(item, Product.class);
                    newProducts.add(product);
                }
            }

            model.addAttribute("products", newProducts);
        }

        model.addAttribute("hide_image", true);
        model.addAttribute("length_products", products.size());
        if (!foundRedis){
            model.addAttribute("products", products);
        }
        model.addAttribute("person", person);

        return "categories.html";
    }
}
