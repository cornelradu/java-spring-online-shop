package elefant.clone.service;

import elefant.clone.model.Product;
import elefant.clone.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Method to get 10 random products
    public List<Product> getRandomProducts() {
        Pageable pageable = PageRequest.of(0, 10); // Fetch 10 products
        return productRepository.findRandomProducts(pageable);
    }

    public List<Product> findByCategoryId(int categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public List<Product> findAll(){
        return productRepository.findAll();
    }
}
