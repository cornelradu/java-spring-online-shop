package elefant.clone.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.context.annotation.Primary;

@Data
@Entity
@Table(name="publishing_house")
public class PublishingHouse extends BaseEntity{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    @Column(name = "id")
    private Long id;

    private String name;

    public void setName(String name) { this.name = name; }

    public String getName() { return name; }
}
