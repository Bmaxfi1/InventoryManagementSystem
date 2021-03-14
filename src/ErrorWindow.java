import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * ErrorWindow is a static class that is used to display a pop-up error window.  It contains one method: display().
 */

public class ErrorWindow {

    /**
     *
     * @param message the message to be displayed in the pop-up window
     */
    public static void display(String message){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(false);
        window.setTitle("Error");
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(message);

        Button okButton = new Button("OK");
        okButton.setOnAction(e -> {
            window.close();
        });

        //Layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, okButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 450,100);
        window.setScene(scene);
        window.show();

    }

}
