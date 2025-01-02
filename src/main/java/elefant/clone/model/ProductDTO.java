package elefant.clone.model;

public class ProductDTO {
    private int id;
    private String name;
    private String description;
    private float price;
    private String image;
    private String listOfAuthors;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getId() {
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

    public float getPrice(){
        return this.price;
    }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }

    public void setListOfAuthors(String listOfAuthors) { this.listOfAuthors = listOfAuthors; }

    public String getListOfAuthors() { return listOfAuthors; }
}
