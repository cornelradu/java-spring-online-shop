package elefant.clone.service;

import elefant.clone.model.Product;
import elefant.clone.model.ProductDTO;
import elefant.clone.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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

    public ProductDTO getProduct(int productId) throws Exception {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new Exception("Product not found"));
        return convertToDTO(product);
    }

    public List<ProductDTO> getProductsByIds(List<Integer> productIds) {
        List<ProductDTO> products = new ArrayList<>();

        Iterable<Product> productLst = productRepository.findAllById(productIds);
        Iterator<Product> itr = productLst.iterator();
        while(itr.hasNext()){
            products.add(convertToDTO(itr.next()));
        }

        return products;
    }

    private ProductDTO convertToDTO(Product product) {
        // Implement conversion logic
        ProductDTO dto = new ProductDTO();
        dto.setId((int)product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setImage(product.getImage());
        dto.setListOfAuthors(product.getListOfAuthors());
        return dto;
    }
}
