import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.List;

/**
 * The Inventory class is used to create an instance of a container that holds parts and products.  It includes various
 * methods which are used to interact with the inventory.
 */

public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList(
            part -> new Observable[]{}
    );
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * @param newPart the new part to be added to the inventory
     */
    public static void addPart(Part newPart) {

        allParts.add(newPart);
    }

    /**
     *
     * @param newProduct the new product to be added to the inventory
     */
    public static void addProduct(Product newProduct) {

        allProducts.add(newProduct);
    }

    /**
     *
     * @param partId the id of the part being searched for
     * @return the part that contains the provided id
     */
    public static Part lookupPart(int partId) {
        int partIndex = -1;
        for (Part part : allParts) {
            if (partId == part.getId()) {
                partIndex = allParts.indexOf(part);
            }
        }
        return allParts.get(partIndex);
    }

    /**
     *
     * @param name the name of the part being searched for
     * @return a list of parts based on the provided name
     */
    public static ObservableList<Part> lookupPart(String name) {

            List<Part> filteredList = new ArrayList<>();
            for (Part part : allParts){
                if(
                    part.getName().toLowerCase().contains(name.toLowerCase())
                )
                    filteredList.add(part);
            }
            return FXCollections.observableList(filteredList);
        }

    /**
     *
      * @param productId the Id of the product being searched for
     * @return the product that contains the provided id
     */
    public static Product lookupProduct(int productId) {
        int productIndex = -1;
        for (Product product : allProducts) {
            if (productId == product.getId()) {
                productIndex = allProducts.indexOf(product);
            }
        }
        return allProducts.get(productIndex);

        };

    /**
     *
     * @param productName the name of the product being searched for
     * @return a list of products based on the provided name
     */
    public static ObservableList<Product> lookupProduct(String productName) {

        List<Product> filteredList = new ArrayList<>();
        for (Product product : allProducts){
            if(
                    product.getName().toLowerCase().contains(productName.toLowerCase())
            )
                filteredList.add(product);
        }
        return FXCollections.observableList(filteredList);

    }

    /**
     *RUNTIME ERROR
     * Parts may be updated from InHouse to Outsourced, or from Outsourced to InHouse.  Because of this, new objects
     * must be created.  Accomplishing this was tricky because the products containing the old part needed to have the
     * respective part removed, and they also needed the new product to be added.  I was able to correct this issue by
     * using nested for loops.  These for loops iterate through each part on each product and makes appropriate changes.
     *
     * @param index the index of the old part
     * @param selectedPart the new part
     */
    public static void updatePart(int index, Part selectedPart) {

        Part oldPart = Inventory.getAllParts().get(index);

        //InHouse to InHouse
        if (Inventory.getAllParts().get(index) instanceof InHouse && selectedPart instanceof InHouse) {
            InHouse oldInHousePart;
            oldInHousePart = InHouse.class.cast(Inventory.getAllParts().get(index));

            oldInHousePart.setId(selectedPart.getId());
            oldInHousePart.setName(selectedPart.getName());
            oldInHousePart.setPrice(selectedPart.getPrice());
            oldInHousePart.setStock(selectedPart.getStock());
            oldInHousePart.setMin(selectedPart.getMin());
            oldInHousePart.setMax(selectedPart.getMax());
            oldInHousePart.setMachineId(((InHouse) selectedPart).getMachineId());

            Inventory.getAllParts().add(oldInHousePart);
            Inventory.getAllParts().remove(index);
        }

        //InHouse to Outsourced
        else if (oldPart instanceof InHouse && selectedPart instanceof Outsourced) {

            Inventory.addPart(selectedPart);

            for (int i = 0; i < Inventory.getAllProducts().size(); i++) {
                int lengthOfPartList = Inventory.getAllProducts().get(i).getAllAssociatedParts().size();
                System.out.println("\nsize of this product's associated parts = " + Inventory.getAllProducts().get(i).getAllAssociatedParts().size());
                int numOfPartsDeleted;
                numOfPartsDeleted = 0;
                for (int i2 = 0; i2 < lengthOfPartList; i2++) {
                    System.out.println("iteration of delete phase start");
                    System.out.println("target part list = " + Inventory.getAllProducts().get(i));
                    System.out.println("i2 = " + i2);
                    if (Inventory.getAllProducts().get(i).getAllAssociatedParts().get(i2 - numOfPartsDeleted) == oldPart){
                        System.out.println("deletion target identified.");
                        Inventory.getAllProducts().get(i).getAllAssociatedParts().remove(i2 - numOfPartsDeleted);
                        numOfPartsDeleted++;
                    }
                    System.out.println("iteration of delete phase end\n");
                }
                System.out.println("all deletion phases complete------------------------\n");
                for (int i2 = -numOfPartsDeleted; i2 < 0; i2++) {
                    System.out.println("iteration of add phase start");
                    Inventory.getAllProducts().get(i).getAllAssociatedParts().add(selectedPart);
                    System.out.println(Inventory.getAllProducts().get(i).getAllAssociatedParts());
                    System.out.println("iteration of add phase end\n");
                }
                System.out.println("all adds complete-----------------------------\n");
            }
            System.out.println("All products updated-------------------------------\n");
            Inventory.deletePart(oldPart);


        }

        //Outsourced to Outsourced
        else if (Inventory.getAllParts().get(index) instanceof Outsourced && selectedPart instanceof Outsourced) {
            Outsourced oldOutsourcedPart;
            oldOutsourcedPart = Outsourced.class.cast(Inventory.getAllParts().get(index));

            oldOutsourcedPart.setId(selectedPart.getId());
            oldOutsourcedPart.setName(selectedPart.getName());
            oldOutsourcedPart.setPrice(selectedPart.getPrice());
            oldOutsourcedPart.setStock(selectedPart.getStock());
            oldOutsourcedPart.setMin(selectedPart.getMin());
            oldOutsourcedPart.setMax(selectedPart.getMax());
            oldOutsourcedPart.setCompanyName(((Outsourced) selectedPart).getCompanyName());

            Inventory.getAllParts().add(oldOutsourcedPart);
            Inventory.getAllParts().remove(index);

        }

        //Outsourced to InHouse
        else if (Inventory.getAllParts().get(index) instanceof Outsourced && selectedPart instanceof InHouse) {
            Inventory.addPart(selectedPart);

            for (int i = 0; i < Inventory.getAllProducts().size(); i++) {
                int lengthOfPartList = Inventory.getAllProducts().get(i).getAllAssociatedParts().size();
                System.out.println("\nsize of this product's associated parts = " + Inventory.getAllProducts().get(i).getAllAssociatedParts().size());
                int numOfPartsDeleted;
                numOfPartsDeleted = 0;
                for (int i2 = 0; i2 < lengthOfPartList; i2++) {
                    System.out.println("iteration of delete phase start");
                    System.out.println("target part list = " + Inventory.getAllProducts().get(i));
                    System.out.println("i2 = " + i2);
                    if (Inventory.getAllProducts().get(i).getAllAssociatedParts().get(i2 - numOfPartsDeleted) == oldPart){
                        System.out.println("deletion target identified.");
                        Inventory.getAllProducts().get(i).getAllAssociatedParts().remove(i2 - numOfPartsDeleted);
                        numOfPartsDeleted++;
                    }
                    System.out.println("iteration of delete phase end\n");
                }
                System.out.println("all deletion phases complete------------------------\n");
                for (int i2 = -numOfPartsDeleted; i2 < 0; i2++) {
                    System.out.println("iteration of add phase start");
                    Inventory.getAllProducts().get(i).getAllAssociatedParts().add(selectedPart);
                    System.out.println(Inventory.getAllProducts().get(i).getAllAssociatedParts());
                    System.out.println("iteration of add phase end\n");
                }
                System.out.println("all adds complete-----------------------------\n");
            }
            System.out.println("All products updated-------------------------------\n");
            Inventory.deletePart(oldPart);

        }


        /*
        System.out.println("updatePart() called");
        Inventory.addPart(selectedPart);
        System.out.println("here are all the parts after add " + Inventory.getAllParts());

        Inventory.deletePart(allParts.get(index));
        System.out.println("Here are all the parts after deleting 'selectedPart'" + Inventory.getAllParts());
        System.out.println("updatePart() finished");
        System.out.println("selected part is " + selectedPart);
        */
    }

    /**
     *
     * @param index the index of the old product
     * @param newProduct the new product
     */
    public static void updateProduct(int index, Product newProduct){
        Inventory.addProduct(newProduct);

        Inventory.allProducts.get(index).getAllAssociatedParts().removeAll();
        allProducts.remove(index);
    }

    /**
     *
     * @param selectedPart the part to be deleted
     */
    public static void deletePart(Part selectedPart) {
        allParts.remove(selectedPart);
    }

    /**
     *
     * @param selectedProduct the product to be deleted
     */
    public static void deleteProduct(Product selectedProduct){
        if (selectedProduct.getAllAssociatedParts().isEmpty()) {
            if (ConfirmWindow.display("Confirmation", "Are you sure you want to delete this product?")) {
                allProducts.remove(selectedProduct);
            }
        }
      else {
          ErrorWindow.display("Please remove all associated parts prior to deletion.");
            }
    };

    /**
     *
     * @return a list of all parts in the inventory
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    };

    /**
     *
     * @return a list of all products in the inventory
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

}

