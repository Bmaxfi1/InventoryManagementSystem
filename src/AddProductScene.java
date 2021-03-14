import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

/**
 * The AddProductScene is a new window that is instantiated upon clicking "Add" on the MainScene window.  It is used to create a new product.
 */

public class AddProductScene {

    Label addProductLabel, idLabel, nameLabel, invLabel, priceLabel, maxLabel, minLabel, partSearchMessage;
    TextField idField, nameField, invField, priceField, maxField, minField, partSearchBox;
    TableView allPartsTable, includedPartsTable;
    Button addPartButton, removePartButton, saveButton, cancelButton;
    Scene addProductScene;

    public AddProductScene() {

        addProductLabel = new Label("Add Product");
        addProductLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
        idLabel = new Label("Id");
        nameLabel = new Label("Name");
        invLabel = new Label("Inv");
        priceLabel = new Label("Price");
        maxLabel = new Label("Max");
        minLabel = new Label("Min");

        idField = new TextField("Auto-generated");
        idField.setDisable(true);
        nameField = new TextField();
        invField = new TextField();
        priceField = new TextField();
        maxField = new TextField();
        minField = new TextField();
        partSearchBox = new TextField();
        partSearchBox.setPromptText("Search by Part ID or Name");
        partSearchBox.setMinWidth(200);

        includedPartsTable = new TableView();

        addPartButton = new Button("Add to Product");
        removePartButton = new Button("Remove from Product");
        saveButton = new Button("Save");
        cancelButton = new Button("Cancel");

        includedPartsTable = new TableView();
        allPartsTable = new TableView();

        partSearchMessage = new Label();
        partSearchMessage.setTextFill(Color.RED);

        Product newProduct = new Product(0, null, 0,0,0,0);

        // all parts table

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

            allPartsTable.setItems(Inventory.getAllParts());
            allPartsTable.getColumns().addAll(idColumn, nameColumn, inventoryLevelColumn, priceColumn);

        //included parts table


            TableColumn<Part, Integer> idColumn2 = new TableColumn<>("Id");
            idColumn2.setMinWidth(20);
            idColumn2.setCellValueFactory(new PropertyValueFactory<>("id")); //this has to be the same value as the property.

            TableColumn<Part, String> nameColumn2 = new TableColumn<>("Name");
            nameColumn2.setMinWidth(150);
            nameColumn2.setCellValueFactory(new PropertyValueFactory<>("name")); //this has to be the same value as the property.

            TableColumn<Part, Integer> inventoryLevelColumn2 = new TableColumn<>("Inventory Level");
            inventoryLevelColumn2.setMinWidth(50);
            inventoryLevelColumn2.setCellValueFactory(new PropertyValueFactory<>("stock")); //this has to be the same value as the property.

            TableColumn<Part, Double> priceColumn2 = new TableColumn<>("Price per Unit");
            priceColumn2.setMinWidth(50);
            priceColumn2.setCellValueFactory(new PropertyValueFactory<>("price"));


            includedPartsTable.getColumns().addAll(idColumn2, nameColumn2, inventoryLevelColumn2, priceColumn2);


        //Search bar functionality////////////////////////////////////////////////////////


        partSearchBox.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (partSearchBox.getText() == "") {
                        allPartsTable.setItems(Inventory.getAllParts());
                    }
                    else if (MiscTools.isInteger(partSearchBox.getText())) {
                        try {


                            int intValue = Integer.parseInt(newValue);
                            Part foundPart = Inventory.lookupPart(intValue);
                            List<Part> filteredList = new ArrayList<>();
                            filteredList.add(foundPart);
                            allPartsTable.setItems(FXCollections.observableList(filteredList));
                        } catch (Exception exception) {
                            System.out.println("search box couldn't find Id");
                        }
                    }
                    else {
                        allPartsTable.setItems(Inventory.lookupPart(newValue));

                    }

                    if (allPartsTable.getItems().isEmpty()) {
                        partSearchMessage.setText("No parts found");
                    }
                    else {partSearchMessage.setText("");}

                }
        );

        //Button Functionality

        addPartButton.setOnAction(e -> {
            try {
                Part selectedPart = (Part) allPartsTable.getSelectionModel().getSelectedItem();
                int idOfPart = selectedPart.getId();
                System.out.println("idOfPart = " + idOfPart);
                System.out.println("selectedPart = " + selectedPart);

                includedPartsTable.getItems().add(Inventory.lookupPart(idOfPart));
                newProduct.addAssociatedPart(Inventory.lookupPart(idOfPart));

                partSearchBox.setText("");
            }
            catch (Exception exception) {
                ErrorWindow.display("Please select a part.");
                System.out.println(exception);
            }
        });

        removePartButton.setOnAction(e -> {
            try {
                int indexOfSelectedPart = includedPartsTable.getSelectionModel().getSelectedIndex();


                boolean decision = ConfirmWindow.display("Delete", "Are you sure you want to remove the selected Part?");
                if (decision) {
                    includedPartsTable.getItems().remove(indexOfSelectedPart);
                    partSearchBox.setText("");
                    ObservableList<Part> allAssociatedParts;
                    allAssociatedParts = newProduct.getAllAssociatedParts();
                    newProduct.deleteAssociatedPart(allAssociatedParts.remove(indexOfSelectedPart));
                }
            }
            catch (Exception exception) {
                if (includedPartsTable.getSelectionModel().getSelectedItem() != null) {
                    ErrorWindow.display("Please select a part.");
                    System.out.println(exception);
                }
            }

        });

        saveButton.setOnAction(e -> {

            //Id generation
            int autoGenId = 0;
            ObservableList<Product> allProducts = Inventory.getAllProducts();
            for (int i = 0; i < allProducts.size(); i++) {
                System.out.println(i);
                if (autoGenId <= allProducts.get(i).getId()) {
                    autoGenId = allProducts.get(i).getId() + 1;
                }
            }

            try {


                if (Integer.parseInt(maxField.getText()) < Integer.parseInt(minField.getText())) {
                    throw new Exception("Min field should be less than or equal to max field.");
                }

                if (Integer.parseInt(invField.getText()) > Integer.parseInt(maxField.getText())) {
                    throw new Exception("Inventory should not exceed the maximum value");
                }

                if (Integer.parseInt(invField.getText()) < Integer.parseInt(minField.getText())) {
                    throw new Exception("Inventory should not fall below the minimum value");
                }

                if (nameField.getText() == "") {
                    throw new Exception("Please enter a name for the product.");
                }


                newProduct.setId(autoGenId);
                newProduct.setName(nameField.getText());
                newProduct.setPrice(Double.parseDouble(priceField.getText()));
                newProduct.setStock(Integer.parseInt(invField.getText()));
                newProduct.setMin(Integer.parseInt(minField.getText()));
                newProduct.setMax(Integer.parseInt(maxField.getText()));

                Inventory.addProduct(newProduct);


                Stage stage = (Stage) getScene().getWindow(); //(Stage) is a downcast, it is being used to reference the stage.
                stage.close();

            }
            catch (Exception exception) {
                ErrorWindow.display("Invalid input.  Please review and try again. \n" + exception.getLocalizedMessage());
            }
        });

        cancelButton.setOnAction(e -> {
            boolean result = ConfirmWindow.display("Return", "Return without saving?");
            if (result) {
                Stage stage = (Stage) getScene().getWindow(); //(Stage) is a downcast, it is being used to reference the stage.
                stage.close();
            }
        });

        //Layout Section/////////////////////////////////
        //GridPane
        GridPane formBundle = new GridPane();

        formBundle.add(idLabel, 0, 0);
        formBundle.add(idField, 1, 0);
        formBundle.add(nameLabel, 0, 1);
        formBundle.add(nameField, 1, 1);
        formBundle.add(invLabel, 0, 2);
        formBundle.add(invField, 1, 2);
        formBundle.add(priceLabel, 0, 3);
        formBundle.add(priceField, 1, 3);
        formBundle.add(maxLabel, 0, 4);
        formBundle.add(maxField, 1, 4);
        formBundle.add(minLabel, 2, 4);
        formBundle.add(minField, 3, 4);

        formBundle.setHgap(5);
        formBundle.setVgap(20);

        HBox addPartHeader = new HBox();
        addPartHeader.getChildren().addAll(addProductLabel);

        HBox footer = new HBox(20);
        footer.setAlignment(Pos.BASELINE_RIGHT);
        footer.getChildren().addAll(saveButton, cancelButton);

        HBox rightSideHeader = new HBox(20);
        rightSideHeader.setAlignment(Pos.BASELINE_RIGHT);
        rightSideHeader.getChildren().addAll(partSearchMessage, partSearchBox);

        VBox leftSide = new VBox(80);
        leftSide.setPadding(new Insets(20,20,20,20));
        leftSide.getChildren().addAll(addPartHeader,formBundle);

        VBox rightSide = new VBox(20);
        rightSide.setPadding(new Insets(10,10,10,10));
        rightSide.setAlignment(Pos.BOTTOM_RIGHT);
        rightSide.getChildren().addAll(rightSideHeader, allPartsTable, addPartButton, includedPartsTable, removePartButton, footer);

        HBox page = new HBox(50);
        page.setPadding(new Insets(20,20,20,20));
        page.getChildren().addAll(leftSide, rightSide);
        page.setStyle("-fx-border-color: black; -fx-padding: 15px; -fx-border-radius: 20px;");

        HBox layout = new HBox(20);
        layout.setPadding(new Insets(30,30,30,30));
        layout.getChildren().addAll(page);

        addProductScene = new Scene(layout, 970, 600);
    }

    /**
     *
     * @return the addProduct scene
     */
    public Scene getScene() {
        return this.addProductScene;
    }

}
