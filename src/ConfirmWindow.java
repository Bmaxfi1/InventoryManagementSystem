import javafx.geometry.*;
import javafx.stage.Modality;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.stage.*;
import javafx.scene.Scene;

/**
 * ConfirmWindow is a static class that is used to display a yes/no window to the user.  It contains one method,
 * display(), which returns a boolean based on user input.
 */

public class ConfirmWindow {

    static boolean answer;

    /**
     *
     * @param title the title of the pop-up window
     * @param message the message contents of the pop-up window
     * @return a boolean value based on user selection
     */
    public static boolean display(String title, String message){

        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(false);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(message);

        Button yesButton = new Button("Yes");
        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });

        Button noButton = new Button("No");
        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });

        HBox hLayout = new HBox(10);
        hLayout.getChildren().addAll(yesButton, noButton);
        hLayout.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, hLayout);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 350, 100);
        window.setScene(scene);
        window.showAndWait();

        return answer;
    };

}
