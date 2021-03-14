import com.sun.glass.ui.Window;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

/**
 * The MainScene Class is the home scene/stage for this application.
 */

public class MainScene {

    Scene mainScene;
    TableView<Part> mainPartsTable;
    TableView<Product> mainProductTable;
    Label inventoryManagementSystemLabel;
    Button exitButton, addPartButton, modifyPartButton, deletePartButton, addProductButton, modifyProductButton, deleteProductButton;
    Label partsLabel;
    Label productsLabel;
    TextField partSearchBox;
    TextField productSearchBox;
    Label partSearchMessage;
    Label productSearchMessage;

    public MainScene() {

        inventoryManagementSystemLabel = new Label("Inventory Management System");
        inventoryManagementSystemLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
        exitButton = new Button("Exit");
        addPartButton = new Button("Add");
        modifyPartButton = new Button("Modify");
        deletePartButton = new Button("Delete");
        addProductButton = new Button("Add");
        modifyProductButton = new Button("Modify");
        deleteProductButton = new Button("Delete");
        partsLabel = new Label("Parts");
        productsLabel = new Label("Products");
        partSearchBox = new TextField();
        partSearchBox.setPromptText("Search parts");
        productSearchBox = new TextField();
        productSearchBox.setPromptText("Search products");

        partSearchMessage = new Label();
        productSearchMessage = new Label();
        partSearchMessage.setTextFill(Color.RED);
        productSearchMessage.setTextFill(Color.RED);


        //Part Table//////////////////////////////////////////////////////////////////////////////////////
        TableColumn<Part, Integer> idColumn = new TableColumn<>("Id");
        idColumn.setMinWidth(20);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id")); //this has to be the same value as the property.

        TableColumn<Part, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(150);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name")); //this has to be the same value as the property.

        TableColumn<Part, Integer> inventoryLevelColumn = new TableColumn<>("Inventory Level");
        inventoryLevelColumn.setMinWidth(50);
        inventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock")); //this has to be the same value as the property.

        TableColumn<Part, Double> priceColumn = new TableColumn<>("Price per Unit");
        priceColumn.setMinWidth(50);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        mainPartsTable = new TableView<>();
        mainPartsTable.setItems(Inventory.getAllParts());
        mainPartsTable.getColumns().addAll(idColumn, nameColumn, inventoryLevelColumn, priceColumn);

        Inventory.getAllParts().addListener(new ListChangeListener<Part>() {
                                                @Override
                                                public void onChanged(Change<? extends Part> change) {
                                                    while (change.next()) {
                                                        if (change.wasUpdated()) {
                                                            System.out.println("change detected");
                                                        }
                                                    }
                                                }
                                            }
        );
        ////////////////////////////////////////////////////////////////////////////////////////////////////////

        //Product Table/////////////////////////////////////////////////////////////////////////////////
        TableColumn<Product, Integer> idColumn2 = new TableColumn<>("Id");
        idColumn2.setMinWidth(20);
        idColumn2.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Product, String> nameColumn2 = new TableColumn<>("Name");
        nameColumn2.setMinWidth(150);
        nameColumn2.setCellValueFactory(new PropertyValueFactory<>("name")); //this has to be the same value as the property.

        TableColumn<Product, Integer> inventoryLevelColumn2 = new TableColumn<>("Inventory Level");
        inventoryLevelColumn2.setMinWidth(50);
        inventoryLevelColumn2.setCellValueFactory(new PropertyValueFactory<>("stock")); //this has to be the same value as the property.

        TableColumn<Product, Double> priceColumn2 = new TableColumn<>("Price per Unit");
        priceColumn2.setMinWidth(50);
        priceColumn2.setCellValueFactory(new PropertyValueFactory<>("price"));

        mainProductTable = new TableView<>();
        mainProductTable.setItems(Inventory.getAllProducts());
        mainProductTable.getColumns().addAll(idColumn2, nameColumn2, inventoryLevelColumn2, priceColumn2);
        ///////////////////////////////////////////////////////////////////////////////////////////////

        //Button Functionality/////////////////////////////////////////////////////////////
        exitButton.setOnAction(e -> {
            boolean result = ConfirmWindow.display("Exit IMS", "Are you sure you want to exit?");
            if (result) {
                System.exit(0);
            }
        });

        addPartButton.setOnAction(e -> {
            AddPartScene addPartScene = new AddPartScene();
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setResizable(false);
            window.setScene(addPartScene.getScene());
            window.setOnCloseRequest(event -> {
                boolean result = ConfirmWindow.display("Return", "Return without saving?");
                event.consume();
                if (result) {
                    window.close();
                }
            });
            window.setTitle("Add Part");
            window.show();
            partSearchBox.setText("");
        });

        modifyPartButton.setOnAction(e -> {
            try {
                Part selectedPart;
                selectedPart = mainPartsTable.getSelectionModel().getSelectedItem();
                int indexOfSelectedPart = mainPartsTable.getSelectionModel().getSelectedIndex();
                System.out.println("You selected " + selectedPart);


                ModifyPartScene modifyPartScene = new ModifyPartScene(indexOfSelectedPart, selectedPart);


                Stage window = new Stage();
                window.initModality(Modality.APPLICATION_MODAL);
                window.setResizable(false);
                window.setScene(modifyPartScene.getScene());
                window.setTitle("Modify Part");
                window.setOnCloseRequest(event -> {
                    boolean result = ConfirmWindow.display("Return", "Return without saving?");
                    event.consume();
                    if (result) {
                        window.close();
                    }
                });
                window.show();

                partSearchBox.setText("");

            } catch (Exception exception) {
                ErrorWindow.display("Please select a part.");
            }

        });

        deletePartButton.setOnAction(e -> {
            boolean partBelongsToProduct = false;

            for (int i = 0; i < Inventory.getAllProducts().size(); i++) {
                int lengthOfPartList = Inventory.getAllProducts().get(i).getAllAssociatedParts().size();
                for (int i2 = 0; i2 < lengthOfPartList; i2++) {
                    if (Inventory.getAllProducts().get(i).getAllAssociatedParts().get(i2) == mainPartsTable.getSelectionModel().getSelectedItem()) {
                        partBelongsToProduct = true;
                    }
                }
            }

            if (partBelongsToProduct) {
                ErrorWindow.display("Cannot delete part.  Please remove the part from all products prior to deletion.");
            } else {
                boolean decision = ConfirmWindow.display("Delete", "Are you sure you want to delete the selected Part?");
                if (decision) {
                    Inventory.deletePart(mainPartsTable.getSelectionModel().getSelectedItem());
                    partSearchBox.setText("");
                }
            }
        });

        addProductButton.setOnAction(e -> {
            AddProductScene addProductScene = new AddProductScene();

            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setResizable(false);
            window.setScene(addProductScene.getScene());
            window.setTitle("Add Product");
            window.setOnCloseRequest(event -> {
                boolean result = ConfirmWindow.display("Return", "Return without saving?");
                event.consume();
                if (result) {
                    window.close();
                }
            });
            window.show();

        });

        modifyProductButton.setOnAction(e -> {

            try {
                Product selectedProduct;
                selectedProduct = mainProductTable.getSelectionModel().getSelectedItem();
                int indexOfSelectedProduct = mainProductTable.getSelectionModel().getSelectedIndex();
                System.out.println("You selected " + selectedProduct);

                ModifyProductScene modifyProductScene = new ModifyProductScene(indexOfSelectedProduct, selectedProduct);

                Stage window = new Stage();
                window.initModality(Modality.APPLICATION_MODAL);
                window.setResizable(false);
                window.setScene(modifyProductScene.getScene());
                window.setTitle("Modify Part");
                window.setOnCloseRequest(event -> {
                    boolean result = ConfirmWindow.display("Return", "Return without saving?");
                    event.consume();
                    if (result) {
                        window.close();
                    }
                });
                window.show();

                partSearchBox.setText("");

            } catch (Exception exception) {
                ErrorWindow.display("Please select a product.");
            }

        });

        deleteProductButton.setOnAction(e -> {
            try {
                Product selectedProduct;
                selectedProduct = mainProductTable.getSelectionModel().getSelectedItem();
                int indexOfSelectedProduct = mainProductTable.getSelectionModel().getSelectedIndex();


                if (selectedProduct.getAllAssociatedParts().size() == 0) {
                    Inventory.deleteProduct(Inventory.getAllProducts().get(indexOfSelectedProduct));
                } else {
                    ErrorWindow.display("Please remove all parts associated with this product prior to deletion.");
                }

            } catch (Exception exception) {
                ErrorWindow.display("Please select a product.");
            }


        });

        //////////////////////////////////////////////////////////////////////////////////

        //Search bar functionality////////////////////////////////////////////////////////

        partSearchBox.textProperty().addListener((observable, oldValue, newValue) -> {


            if (partSearchBox.getText() == "") {
                mainPartsTable.setItems(Inventory.getAllParts());
                partSearchMessage.setText("");
            } else if (MiscTools.isInteger(partSearchBox.getText())) {
                try {
                    int intValue = Integer.parseInt(newValue);
                    Part foundPart = Inventory.lookupPart(intValue);
                    List<Part> filteredList = new ArrayList<>();
                    filteredList.add(foundPart);
                    mainPartsTable.setItems(FXCollections.observableList(filteredList));
                } catch (Exception exception) {
                    System.out.println("search box couldn't find Id");
                }
            } else {
                        mainPartsTable.setItems(Inventory.lookupPart(newValue));

                    }

                    if (mainPartsTable.getItems().isEmpty()) {
                        partSearchMessage.setText("No parts found");
                    } else {
                        partSearchMessage.setText("");
                    }

                }
        );

        productSearchBox.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (productSearchBox.getText() == "") {
                        mainProductTable.setItems(Inventory.getAllProducts());
                    } else if (MiscTools.isInteger(productSearchBox.getText())) {
                        try {
                            int intValue = Integer.parseInt(newValue);
                            Product foundProduct = Inventory.lookupProduct(intValue);
                            List<Product> filteredList = new ArrayList<>();
                            filteredList.add(foundProduct);
                            mainProductTable.setItems(FXCollections.observableList(filteredList));
                        } catch (Exception exception) {
                            System.out.println("search box couldn't find Id");
                        }
                    } else {
                        mainProductTable.setItems(Inventory.lookupProduct(newValue));

                    }
                    if (mainProductTable.getItems().isEmpty()) {
                        productSearchMessage.setText("No products found");
                    } else {
                        productSearchMessage.setText("");
                    }


                }
        );

        //////////////////////////////////////////////////////////////////////////////////

        //Layout Section
        HBox partsTableHeader = new HBox(10);
        partsTableHeader.getChildren().addAll(partsLabel, partSearchBox, partSearchMessage);

        HBox productsTableHeader = new HBox(10);
        productsTableHeader.getChildren().addAll(productsLabel, productSearchBox, productSearchMessage);

        HBox partsTableFooter = new HBox(10);
        partsTableFooter.getChildren().addAll(addPartButton, modifyPartButton, deletePartButton);
        partsTableFooter.setAlignment(Pos.CENTER_RIGHT);

        HBox productTableFooter = new HBox(10);
        productTableFooter.getChildren().addAll(addProductButton, modifyProductButton, deleteProductButton);
        productTableFooter.setAlignment(Pos.CENTER_RIGHT);

        VBox partsTableBundle = new VBox(10);
        partsTableBundle.getChildren().addAll(partsTableHeader, mainPartsTable, partsTableFooter);
        partsTableBundle.setStyle("-fx-border-color: black; -fx-padding: 15px; -fx-border-radius: 20px;");

        VBox productTableBundle = new VBox(10);
        productTableBundle.getChildren().addAll(productsTableHeader, mainProductTable, productTableFooter);
        productTableBundle.setStyle("-fx-border-color: black; -fx-padding: 15px; -fx-border-radius: 20px;");

        HBox tableRow = new HBox(50);
        tableRow.getChildren().addAll(partsTableBundle, productTableBundle);
        tableRow.setAlignment(Pos.CENTER);

        HBox footerRow = new HBox();
        footerRow.getChildren().addAll(exitButton);
        footerRow.setAlignment(Pos.CENTER_RIGHT);


        VBox layout = new VBox(20);
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.getChildren().addAll(inventoryManagementSystemLabel, tableRow, footerRow);

        mainScene = new Scene(layout, 1000, 350);


    }

    public Scene getScene() {
        return mainScene;
    }

    ;

    public void refreshTables() {
        mainProductTable.refresh();
        mainPartsTable.refresh();
    }

    ;
}
