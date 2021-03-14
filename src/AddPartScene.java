import javafx.collections.ObservableList;
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
 * The AddPartScene is a new window that is instantiated upon clicking "Add" on the MainScene window.  It is used to create a new part.
 */

public class AddPartScene {

    Label addPartLabel, idLabel, nameLabel, invLabel, priceLabel, maxLabel, minLabel, contextualLabel;
    RadioButton inHouseRadioButton, outsourcedRadioButton;
    TextField idField, nameField, invField, priceField, maxField, minField, contextualField;
    Button saveButton, cancelButton;
    Scene addPartScene;

    public AddPartScene() {

        addPartLabel = new Label("Add Part");
        addPartLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
        idLabel = new Label("Id");
        nameLabel = new Label("Name");
        invLabel = new Label("Inventory");
        priceLabel = new Label("Price");
        maxLabel = new Label("Max");
        minLabel = new Label("Min");
        contextualLabel = new Label("Machine Id ");

        inHouseRadioButton = new RadioButton("In-House");
        outsourcedRadioButton = new RadioButton("Outsourced");

        idField = new TextField();
        idField.setText("Auto-generated");
        idField.setDisable(true);

        nameField = new TextField();
        invField = new TextField();
        priceField = new TextField();
        maxField = new TextField();
        minField = new TextField();
        contextualField = new TextField();

        saveButton = new Button("Save");
        cancelButton = new Button("Cancel");

        //Radio Button Logic/////////////////////////////////////////////////////////////////
        ToggleGroup toggleGroup = new ToggleGroup();

        inHouseRadioButton.setToggleGroup(toggleGroup);
        outsourcedRadioButton.setToggleGroup(toggleGroup);

        inHouseRadioButton.setSelected(true);

        inHouseRadioButton.setOnAction(e ->
                contextualLabel.setText("Machine Id  ")
                );
        outsourcedRadioButton.setOnAction(e ->
                contextualLabel.setText("Company Name")
                );

        //Button Logic//////////////////////////////////////////////////////////////////////
        cancelButton.setOnAction(e -> {
            boolean result = ConfirmWindow.display("Return", "Return without saving?");
            if (result) {
                Stage stage = (Stage) getScene().getWindow(); //(Stage) is a downcast, it is being used to reference the stage.
                stage.close();
            }
        });

        saveButton.setOnAction(e -> {

            //Id generation
            int autoGenId = 0;

            ObservableList<Part> allParts = Inventory.getAllParts();
            for (int i = 0; i < allParts.size(); i++) {
                System.out.println(i);
                if (autoGenId <= allParts.get(i).getId()) {
                    autoGenId = allParts.get(i).getId() + 1;
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
                    throw new Exception("Please enter a name for the part.");
                }

                if (inHouseRadioButton.isSelected()) {
                    Part newPart = new InHouse(
                            autoGenId,
                            nameField.getText(),
                            Double.parseDouble(priceField.getText()),
                            Integer.parseInt(invField.getText()),
                            Integer.parseInt(minField.getText()),
                            Integer.parseInt(maxField.getText()),
                            Integer.parseInt(contextualField.getText()));

                    Inventory.addPart(newPart);
                }

                if (outsourcedRadioButton.isSelected()) {
                    Part newPart = new Outsourced(
                            autoGenId,
                            nameField.getText(),
                            Double.parseDouble(priceField.getText()),
                            Integer.parseInt(invField.getText()),
                            Integer.parseInt(minField.getText()),
                            Integer.parseInt(maxField.getText()),
                            contextualField.getText());

                    Inventory.addPart(newPart);
                }

                Stage stage = (Stage) getScene().getWindow(); //(Stage) is a downcast, it is being used to reference the stage.
                stage.close();

            } catch(Exception exception) {
                ErrorWindow.display("Invalid input.  Please review and try again. \n" + exception.getLocalizedMessage());;
            }
        });

        //Layout Section/////////////////////////////////////////////////////////////////////////
        HBox header = new HBox(30);
        header.getChildren().addAll(addPartLabel, inHouseRadioButton, outsourcedRadioButton);

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

        addPartScene = new Scene(layout, 550, 400);
    }

    /**
     *
     * @return The addPart scene
     */
    public Scene getScene() {

        return this.addPartScene;
    }

};
