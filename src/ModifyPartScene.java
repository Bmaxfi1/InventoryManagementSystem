import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * The ModifyPartScene is a new window that is instantiated upon clicking "Modify" on the MainScene window.  It is used
 * to modify a part.
 */

public class ModifyPartScene {
    Label modifyPartLabel, idLabel, nameLabel, invLabel, priceLabel, maxLabel, minLabel, contextualLabel;
    RadioButton inHouseRadioButton, outsourcedRadioButton;
    TextField idField, nameField, invField, priceField, maxField, minField, contextualField;
    Button saveButton, cancelButton;
    Scene modifyPartScene;

    public ModifyPartScene(int indexOfSelectedPart, Part selectedPart) {

        modifyPartLabel = new Label("Modify Part");
        modifyPartLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
        idLabel = new Label("Id");
        nameLabel = new Label("Name");
        invLabel = new Label("Inventory");
        priceLabel = new Label("Price");
        maxLabel = new Label("Max");
        minLabel = new Label("Min");
        contextualLabel = new Label();

        inHouseRadioButton = new RadioButton("In-House");
        outsourcedRadioButton = new RadioButton("Outsourced");

        idField = new TextField();
        idField.setText(String.valueOf(selectedPart.getId()));
        idField.setDisable(true);

        nameField = new TextField(selectedPart.getName());
        invField = new TextField(String.valueOf(selectedPart.getStock()));
        priceField = new TextField(String.valueOf(selectedPart.getPrice()));
        maxField = new TextField(String.valueOf(selectedPart.getMax()));
        minField = new TextField(String.valueOf(selectedPart.getMin()));
        contextualField = new TextField();
                if (selectedPart instanceof InHouse) {
                    inHouseRadioButton.setSelected(true);
                    contextualLabel.setText("MachineId  ");
                    contextualField.setText(String.valueOf(((InHouse)selectedPart).getMachineId()));
                }
                if (selectedPart instanceof Outsourced){
                    outsourcedRadioButton.setSelected(true);
                    contextualLabel.setText("Company Name");
                    contextualField.setText(((Outsourced)selectedPart).getCompanyName());
                }
        ;

        saveButton = new Button("Save");
        cancelButton = new Button("Cancel");
        

        //Radio Button Logic
        ToggleGroup toggleGroup = new ToggleGroup();

        inHouseRadioButton.setToggleGroup(toggleGroup);
        outsourcedRadioButton.setToggleGroup(toggleGroup);


        inHouseRadioButton.setOnAction(e ->
                contextualLabel.setText("Machine Id  ")
        );
        outsourcedRadioButton.setOnAction(e ->
                contextualLabel.setText("Company Name")
        );

        //Button Logic
        cancelButton.setOnAction(e -> {
            boolean result = ConfirmWindow.display("Return", "Return without saving?");
            if (result) {
                Stage stage = (Stage) getScene().getWindow(); //(Stage) is a downcast, it is being used to reference the stage.
                stage.close();
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
                    throw new Exception("Please enter a name for the part.");
                }

                if (selectedPart instanceof InHouse) {

                    //if staying InHouse
                    if (inHouseRadioButton.isSelected()) {
                        Part newPart = new InHouse(
                                selectedPart.getId(),
                                nameField.getText(),
                                Double.parseDouble(priceField.getText()),
                                Integer.parseInt(invField.getText()),
                                Integer.parseInt(minField.getText()),
                                Integer.parseInt(maxField.getText()),
                                Integer.parseInt(contextualField.getText())
                        );
                        Inventory.updatePart(indexOfSelectedPart , newPart);
                    }

                    //if becoming outsourced
                    if (outsourcedRadioButton.isSelected()){
                        Part newPart = new Outsourced(
                                selectedPart.getId(),
                                nameField.getText(),
                                Double.parseDouble(priceField.getText()),
                                Integer.parseInt(invField.getText()),
                                Integer.parseInt(minField.getText()),
                                Integer.parseInt(maxField.getText()),
                                contextualField.getText()
                        );

                        Inventory.updatePart(indexOfSelectedPart , newPart);
                    }
                }

                if (selectedPart instanceof Outsourced) {

                    //if staying Outsourced
                    if (outsourcedRadioButton.isSelected()) {
                        Part newPart = new Outsourced(
                                selectedPart.getId(),
                                nameField.getText(),
                                Double.parseDouble(priceField.getText()),
                                Integer.parseInt(invField.getText()),
                                Integer.parseInt(minField.getText()),
                                Integer.parseInt(maxField.getText()),
                                contextualField.getText()
                        );
                        Inventory.updatePart(indexOfSelectedPart , newPart);
                    }
                    //if becoming InHouse
                    if (inHouseRadioButton.isSelected()) {

                        Part newPart = new InHouse(
                                selectedPart.getId(),
                                nameField.getText(),
                                Double.parseDouble(priceField.getText()),
                                Integer.parseInt(invField.getText()),
                                Integer.parseInt(minField.getText()),
                                Integer.parseInt(maxField.getText()),
                                Integer.parseInt(contextualField.getText())
                                );
                        Inventory.updatePart(indexOfSelectedPart , newPart);
                    }
                }
                Stage stage = (Stage) getScene().getWindow();
                stage.close();

            } catch(Exception exception) {
                ErrorWindow.display("Invalid input.  Please review and try again. \n" + exception.getLocalizedMessage());;
            }
        });

        //Layout Section////////////////////////////////////////////////////
        HBox header = new HBox(30);
        header.getChildren().addAll(modifyPartLabel, inHouseRadioButton, outsourcedRadioButton);

        //GridPane
        GridPane formBundle = new GridPane();
        ColumnConstraints column0 = new ColumnConstraints();
        column0.setMinWidth(90);
        formBundle.getColumnConstraints().addAll(column0);
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
        formBundle.add(contextualLabel, 0, 5);
        formBundle.add(contextualField, 1, 5);
        formBundle.setHgap(15);
        formBundle.setVgap(10);
        formBundle.setPadding(new Insets(10));

        HBox footer = new HBox(20);
        footer.getChildren().addAll(saveButton, cancelButton);
        footer.setAlignment(Pos.BASELINE_RIGHT);

        VBox layout = new VBox(30);
        layout.setPadding(new Insets(30,30,30,30));
        layout.getChildren().addAll(header, formBundle, footer);

        modifyPartScene = new Scene(layout, 550, 400);
    }

    /**
     *
     * @return the modifyPartScene
     */
    public Scene getScene() {
        return this.modifyPartScene;
    }
}


