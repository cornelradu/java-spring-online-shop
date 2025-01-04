package elefant.clone.controller;

import elefant.clone.model.Category;
import elefant.clone.model.Product;
import elefant.clone.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DashboardEditProductController  {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    PublishingHouseRepository publishingHouseRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    AuthorPairingRepository authorPairingRepository;

    @Autowired
    CategoryPairingRepository categoryPairingRepository;

    @GetMapping("/dashboard/products/edit/{id}")
    public String editProduct(@PathVariable int id, Model model) {
        Product product = productRepository.findById(id).get();
        model.addAttribute("product", product);
        model.addAttribute("publishingHouses", publishingHouseRepository.findAll());
        model.addAttribute("authors", authorRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("hide_image", true);
        return "dashboard_edit.html";
    }

    @PostMapping("/dashboard/products/update")
    @Transactional
    public String updateProduct(@Valid @ModelAttribute Product product, BindingResult result) {
        if (result.hasErrors()) {
            return "dashboard_edit.html";
        }

        Product existingProduct = productRepository.findById((int)product.getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Clear existing author pairings
        authorPairingRepository.deleteByProductId(product.getId());
        categoryPairingRepository.deleteByProductId(product.getId());


        productRepository.save(product);
        return "redirect:/dashboard/products/edit/" + product.getId();
    }

    @GetMapping("/dashboard/products/search-form")
    public String showSearchForm(Model model) {
        model.addAttribute("hide_image", true);
        return "dashboard_search.html";
    }

    @GetMapping("/dashboard/products/search")
    public String searchProduct(@RequestParam String name, RedirectAttributes redirectAttributes) {
        // Find first product that contains the name (case-insensitive)
        Product product = productRepository.findFirstByNameContainingIgnoreCase(name)
                .orElse(null);

        if (product != null) {
            return "redirect:/dashboard/products/edit/" + product.getId();
        } else {
            // If no product found, redirect back to search with error
            redirectAttributes.addFlashAttribute("error", "No product found with name: " + name);
            return "redirect:/dashboard/products/search-form";
        }
    }

    @GetMapping("/dashboard/categories")
    public String showCategories(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("hide_image", true);
        return "dashboard_categories.html";  // this refers to your template name
    }

    @PostMapping("/dashboard/categories/add")
    public String addCategory(@RequestParam String categoryName, RedirectAttributes redirectAttributes) {
        try {
            // Check if category already exists
            if (categoryRepository.findByCategoryNameIgnoreCase(categoryName).isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Category already exists: " + categoryName);
                return "redirect:/dashboard/categories";
            }

            // Create and save new category
            Category category = new Category();
            category.setCategoryName(categoryName);
            categoryRepository.save(category);

            redirectAttributes.addFlashAttribute("success", "Category added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding category: " + e.getMessage());
        }

        return "redirect:/dashboard/categories";
    }

    @PostMapping("/dashboard/categories/delete/{id}")
    public String deleteCategory(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            if (!category.getCategoryPairings().isEmpty()) {
                redirectAttributes.addFlashAttribute("error",
                        "Cannot delete category that has associated products");
                return "redirect:/dashboard/categories";
            }

            categoryRepository.delete(category);
            redirectAttributes.addFlashAttribute("success", "Category deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting category: " + e.getMessage());
        }

        return "redirect:/dashboard/categories";
    }
}
