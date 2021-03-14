import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The Product class is used to define the parameters, methods, and structure of a particular product instance.
 */


public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    public Product(int id, String name, double price, int stock, int min, int max){
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     *
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     *
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     *
     * @param min the minimum inventory to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     *
     * @param max the maximum inventory to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     *
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     *
     * @return the minimum allowable inventory
     */
    public int getMin() {
        return min;
    }

    /**
     *
     * @return the maximum allowable inventory
     */
    public int getMax() {
        return max;
    }

    /**
     *
     * @param part the part to add to the associated part list
     */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    };

    /**
     *
     * @param selectedAssociatedPart the part to delete from the product
     */
    public void deleteAssociatedPart(Part selectedAssociatedPart){
        associatedParts.remove(selectedAssociatedPart);
    };

    /**
     *
     * @return a list of all associated parts for the product
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    };

}
