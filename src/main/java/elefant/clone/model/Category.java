package elefant.clone.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Data
@Entity
@Table(name="category")
@JsonIgnoreProperties(ignoreUnknown = true)

public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    private Long id;

    private String categoryName;

    @JsonIgnoreProperties("category")
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<CategoryPairing> categoryPairings;

    public String getCategoryName(){
        return categoryName;
    }

    public long getId(){
        return id;
    }

    public List<CategoryPairing> getCategoryPairings(){
        return categoryPairings;
    }

    public void setCategoryName(String categoryName){
        this.categoryName = categoryName;
    }

    public void setCategoryPairings(List<CategoryPairing> categoryPairings){
        this.categoryPairings = categoryPairings;
    }

}
