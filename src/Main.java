import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;

/**
 * @author Brandon Maxfield
 */

/**
 * Main is the starting point of the application.
 *
 * FUTURE ENHANCEMENT
 * There will be an update to the application which will allow it to interact with a local or server database.
 *
 * FUTURE ENHANCEMENT
 * When deleting a part or product, if that item has associated parts/products, instead of simply showing an error
 * message the user will have the option to delete all associated items from a pop-up window.
 *
 */

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        loadDefaultInventory();

        MainScene mainScene = new MainScene();
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(mainScene.getScene());
        primaryStage.setOnCloseRequest(e -> {
            boolean result = ConfirmWindow.display("Exit IMS", "Are you sure you want to exit?");
            e.consume();
            if (result) {
                System.exit(0);
            }
        });
        primaryStage.show();


        //refreshing tables
        primaryStage.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean onHidden, Boolean onShown)
            {
                System.out.println("event detected");
                mainScene.refreshTables();
            }
        });

    }

    /**
     * @return a demo list of parts and products.
     */
    public void loadDefaultInventory(){
        Part gear = new InHouse(1, "Gear", 29.99,5, 1, 99, 1);
        Part spring = new Outsourced(2,"Spring", 17.99, 3, 1, 99, "Springs R us");
        Part lever = new InHouse(3,"Lever", 12.99, 19, 1, 99, 2);

        Inventory inventory = new Inventory();
        inventory.addPart(gear);
        inventory.addPart(spring);
        inventory.addPart(lever);

        Product gadget = new Product(1, "Gadget", 89.99, 56, 1,99);
        gadget.addAssociatedPart(gear);
        gadget.addAssociatedPart(spring);
        gadget.addAssociatedPart(lever);
        gadget.addAssociatedPart(gear);
        gadget.addAssociatedPart(spring);
        gadget.addAssociatedPart(lever);

        Product gizmo = new Product(2,"Gizmo", 119.99, 20, 1, 99);
        gizmo.addAssociatedPart(gear);
        gizmo.addAssociatedPart(spring);
        gizmo.addAssociatedPart(gear);
        gizmo.addAssociatedPart(lever);
        gizmo.addAssociatedPart(gear);

        inventory.addProduct(gadget);
        inventory.addProduct(gizmo);
    }


}
