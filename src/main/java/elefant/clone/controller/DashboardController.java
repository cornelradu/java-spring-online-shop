package elefant.clone.controller;

import elefant.clone.model.*;
import elefant.clone.repository.*;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    @Value("${upload_dir}")
    public final String UPLOAD_DIR;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AuthorPairingRepository authorPairingRepository;
    @Autowired
    private PublishingHouseRepository publishingHouseRepository;

    @Autowired
    private CategoryPairingRepository categoryPairingRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public DashboardController(@Value("${upload_dir}") String UPLOAD_DIR) {
        this.UPLOAD_DIR = UPLOAD_DIR;
    }


    @RequestMapping("/dashboard")
    public String displayDashboard(Model model, Authentication authentication, HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = personRepository.readByName(auth.getName());

        if(!person.getRoles().getRoleName().equals("Admin")) {
            return "redirect:/home";
        }

        List<Category> categories = categoryRepository.findAll();
        String categoriesString = categories.stream().map(Category::getCategoryName).collect(Collectors.joining(","));
        model.addAttribute("categories", categoriesString);
        model.addAttribute("hide_image", true);
        return "dashboard.html";
    }

    @PostMapping("/dashboard/books")
    public String handleBookSubmission(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("author") String author,
            @RequestParam("publishing_house") String publishing_house,
            @RequestParam("price") double price,
            @RequestParam("image") MultipartFile image,
            @RequestParam("category") String category,
            Model model) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = personRepository.readByName(auth.getName());

        if(!person.getRoles().getRoleName().equals("Admin")) {
            return "redirect:/home";
        }

        Product p = new Product();
        p.setName(name);
        p.setDescription(description);
        p.setPrice((float)price);

        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Save the uploaded file
        String fileName = image.getOriginalFilename();
        if (fileName != null && !fileName.isEmpty()) {
            Path filePath = uploadPath.resolve(fileName);
            image.transferTo(filePath.toFile());
            System.out.println("Image saved to: " + filePath.toAbsolutePath());
            p.setImage(filePath.getName(filePath.getNameCount()-1).toString());
        }

        p.setCreatedBy(1);
        p.setUpdatedBy(1);
        List<PublishingHouse> foundPublishingHouse = publishingHouseRepository.findByName(publishing_house);

        if(foundPublishingHouse.size() == 0) {
            PublishingHouse newPublishingHouse = new PublishingHouse();
            newPublishingHouse.setName(publishing_house);
            newPublishingHouse.setCreatedBy(1);
            newPublishingHouse.setUpdatedBy(1);
            PublishingHouse savedPublishingHouse = publishingHouseRepository.save(newPublishingHouse);
            p.setPublishingHouse(savedPublishingHouse);
        } else {
            p.setPublishingHouse(foundPublishingHouse.get(0));
        }
        Product savedProduct = productRepository.save(p);

        for(String authorName : author.split(",")) {
            String stripAuthorName = new String(authorName.trim());
            List<Author> foundAuthors = authorRepository.findByName(stripAuthorName);
            if(foundAuthors.size()  == 0){
                Author newAuthor = new Author();
                newAuthor.setName(stripAuthorName);
                newAuthor.setCreatedBy(1);
                newAuthor.setUpdatedBy(1);
                Author savedAuthor = authorRepository.save(newAuthor);

                AuthorPairing authorPairing = new AuthorPairing();
                authorPairing.setProduct(savedProduct);
                authorPairing.setAuthor(savedAuthor);
                authorPairing.setCreatedBy(1);
                authorPairing.setUpdatedBy(1);
                authorPairingRepository.save(authorPairing);
            } else {
                AuthorPairing authorPairing = new AuthorPairing();
                authorPairing.setCreatedBy(1);
                authorPairing.setUpdatedBy(1);
                authorPairing.setProduct(savedProduct);
                authorPairing.setAuthor(foundAuthors.get(0));
                authorPairingRepository.save(authorPairing);
            }
        }

        for(String categoryName : category.split(",")) {
            String stripCategoryName = new String(categoryName.trim());
            List<Category> foundCategories = categoryRepository.findByCategoryName(stripCategoryName);
            if(foundCategories.size() == 0){
                Category newCategory = new Category();
                newCategory.setCategoryName(stripCategoryName);
                newCategory.setCreatedBy(1);
                newCategory.setUpdatedBy(1);
                Category savedCategory = categoryRepository.save(newCategory);

                CategoryPairing categoryPairing = new CategoryPairing();
                categoryPairing.setProduct(savedProduct);
                categoryPairing.setCategory(savedCategory);
                categoryPairing.setCreatedBy(1);
                categoryPairing.setUpdatedBy(1);
                categoryPairingRepository.save(categoryPairing);
            } else{
                Category foundCategory = foundCategories.get(0);
                CategoryPairing categoryPairing = new CategoryPairing();
                categoryPairing.setProduct(savedProduct);
                categoryPairing.setCategory(foundCategory);
                categoryPairing.setCreatedBy(1);
                categoryPairing.setUpdatedBy(1);
                categoryPairingRepository.save(categoryPairing);
            }
        }

        // For now, let's log the details
        System.out.println("Book Name: " + name);
        System.out.println("Description: " + description);
        System.out.println("Author: " + author);
        System.out.println("Price: " + price);
        System.out.println("Image Name: " + image.getOriginalFilename());

        // Add attributes to the model if needed
        model.addAttribute("message", "Book successfully submitted!");

        // Redirect or show a success page
        return "redirect:/dashboard";
    }
}
