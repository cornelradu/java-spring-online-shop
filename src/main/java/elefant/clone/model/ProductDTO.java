package elefant.clone.model;

public class ProductDTO {
    private long id;
    private String name;
    private String description;
    private float price;
    private String image;
    private String listOfAuthors;
    private float averageRating;

    public void setAverageRating(float averageRating) { this.averageRating = averageRating; }

    public float getAverageRating() { return averageRating; }

    public void setId(long id) {
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

    public long getId() {
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
