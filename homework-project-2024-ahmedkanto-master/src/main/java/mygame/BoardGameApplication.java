package mygame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
/**
 * The Application class extra starter.
 */
public class BoardGameApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);


            scene.setOnKeyPressed(event -> {
                BoardGameController controller = loader.getController();
                controller.handleKeyPress(event);
            });

            primaryStage.setScene(scene);
            primaryStage.setTitle("Puzzle Game");
            primaryStage.show();


            root.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}