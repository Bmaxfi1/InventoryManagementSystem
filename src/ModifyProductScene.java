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
 * The ModifyProductScene class is a new window that is instantiated upon clicking "Modify" on the MainScene window.
 */

public class ModifyProductScene {
    
        Label modifyProductLabel, idLabel, nameLabel, invLabel, priceLabel, maxLabel, minLabel, partSearchMessage;
        TextField idField, nameField, invField, priceField, maxField, minField, partSearchBox;
        TableView allPartsTable, includedPartsTable;
        Button addPartButton, removePartButton, saveButton, cancelButton;

        Scene modifyProductScene;

        public ModifyProductScene(int indexOfSelectedProduct, Product selectedProduct) {

            modifyProductLabel = new Label("Modify Product");
            modifyProductLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
            idLabel = new Label("Id");
            nameLabel = new Label("Name");
            invLabel = new Label("Inv");
            priceLabel = new Label("Price");
            maxLabel = new Label("Max");
            minLabel = new Label("Min");

            idField = new TextField("Auto-generated");
            idField.setText(String.valueOf(selectedProduct.getId()));
            idField.setDisable(true);

            nameField = new TextField(selectedProduct.getName());
            invField = new TextField(String.valueOf(selectedProduct.getStock()));
            priceField = new TextField(String.valueOf(selectedProduct.getPrice()));
            maxField = new TextField(String.valueOf(selectedProduct.getMax()));
            minField = new TextField(String.valueOf(selectedProduct.getMin()));
            partSearchBox = new TextField();
            partSearchBox.setPromptText("Search by Part ID or Name");
            partSearchBox.setMinWidth(200);

            addPartButton = new Button("Add to Product");
            removePartButton = new Button("Remove from Product");
            saveButton = new Button("Save");
            cancelButton = new Button("Cancel");

            includedPartsTable = new TableView();
            allPartsTable = new TableView();

            partSearchMessage = new Label();
            partSearchMessage.setTextFill(Color.RED);

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

            allPartsTable.getColumns().addAll(idColumn, nameColumn, inventoryLevelColumn, priceColumn);
            allPartsTable.setItems(Inventory.getAllParts());


            //included parts table

            ObservableList<Part> shallowIncludedPartsTable = FXCollections.observableArrayList(selectedProduct.getAllAssociatedParts());

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
            includedPartsTable.setItems(shallowIncludedPartsTable);


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

            //Button Logic

            addPartButton.setOnAction(e -> {
                try {
                    Part selectedPart = (Part) allPartsTable.getSelectionModel().getSelectedItem();
                    int idOfPart = selectedPart.getId();

                    includedPartsTable.getItems().add(Inventory.lookupPart(idOfPart));
                    selectedProduct.addAssociatedPart(Inventory.lookupPart(idOfPart));

                    partSearchBox.setText("");
                }
                catch (Exception exception) {
                    ErrorWindow.display("Please select a part.");
                }
            });

            removePartButton.setOnAction(e -> {
                try {
                    int indexOfSelectedPart = includedPartsTable.getSelectionModel().getSelectedIndex();

                    boolean decision = ConfirmWindow.display("Delete", "Are you sure you want to remove the selected Part?");
                    if (decision) {
                        includedPartsTable.getItems().remove(indexOfSelectedPart);
                        partSearchBox.setText("");
                    }


                }
                catch (Exception exception) {
                    ErrorWindow.display("Please select a part.");
                }

            });

            saveButton.setOnAction(e -> {
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
                        selectedProduct.setName(nameField.getText());
                        selectedProduct.setPrice(Double.valueOf(priceField.getText()));
                        selectedProduct.setStock(Integer.parseInt(invField.getText()));
                        selectedProduct.setMin(Integer.parseInt(minField.getText()));
                        selectedProduct.setMax(Integer.parseInt(maxField.getText()));

                        //part list modifications
                        int initialTableSize = includedPartsTable.getItems().size();
                        int initialExistingPartListSize = selectedProduct.getAllAssociatedParts().size();

                        for (int i = 0; i < initialExistingPartListSize; i++) {
                            selectedProduct.deleteAssociatedPart(selectedProduct.getAllAssociatedParts().get(0));
                        }

                    for (int i = 0; i < initialTableSize; i++){
                        selectedProduct.addAssociatedPart(Part.class.cast(includedPartsTable.getItems().get(i)));
                        }

                    Inventory.updateProduct(indexOfSelectedProduct, selectedProduct);

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
            ///////////////////////////////////////////////

            HBox addPartHeader = new HBox();
            addPartHeader.getChildren().addAll(modifyProductLabel);

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

            modifyProductScene = new Scene(layout, 970, 600);
        }

    /**
     *
     * @return the modifyProductScene
     */
    public Scene getScene() {
            return this.modifyProductScene;
        }

    }


