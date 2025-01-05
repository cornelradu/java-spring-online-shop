package elefant.clone.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name="product")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    private Long id;

    private String name;

    private String description;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST, targetEntity = PublishingHouse.class)
    @JoinColumn(name = "publishing_house", referencedColumnName = "id",nullable = false)

    private PublishingHouse publishingHouse;

    private String image;

    @JsonIgnoreProperties("product")
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<AuthorPairing> authorPairings;

    @JsonIgnoreProperties("product")
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<CategoryPairing> categoryPairings;

    @JsonManagedReference
    @JsonIgnoreProperties("product")

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Vote> votes;


    private float price;

    public void setPrice(float price){
        this.price = price;
    }

    public float getPrice(){
        return this.price;
    }

    public String getListOfAuthors(){
        StringBuilder sb = new StringBuilder();
        for(AuthorPairing authorPairing : authorPairings){
            sb.append(authorPairing.getAuthor().getName()).append(", ");
        }
        if(sb.length() == 0) return "";
        return sb.toString().substring(0, sb.toString().length() - 2);
    }

    public List<AuthorPairing> getAuthorPairings() {
        return authorPairings;
    }

    public Long getId() {
        return id;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public String getImage(){
        return image;
    }

    public void setName(String name) { this.name = name; }

    public void setDescription(String description) { this.description = description; }

    public void setImage(String image) { this.image = image; }

    public void setPublishingHouse(PublishingHouse publishingHouse){
        this.publishingHouse = publishingHouse;
    }

    public PublishingHouse getPublishingHouse(){
        return publishingHouse;
    }

    public void setAuthorPairings(List<AuthorPairing> authorPairings){
        this.authorPairings = authorPairings;
    }

    public void setCategoryPairings(List<CategoryPairing> categoryPairings){
        this.categoryPairings = categoryPairings;
    }

    public List<CategoryPairing> getCategoryPairings(){
        return categoryPairings;
    }

    public void setId(Long id) { this.id = id; }

    public float getAverageRating(){
        float rating = 0;
        for(Vote vote : votes){
            rating += vote.getRating();
        }
        if(votes.size() == 0){
            return 0;
        }
        return rating/votes.size();
    }

    @Override
    public String toString(){
        return name;
    }
}
