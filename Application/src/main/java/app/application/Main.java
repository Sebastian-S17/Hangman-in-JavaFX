package app.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    private static final int w=600;
    private static final int h=500;
    public static Stage stage;
    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        Image icon = new Image("logo.png");
        stage.getIcons().add(icon);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("game.fxml")));
        stage.setTitle("Hangman");
        stage.setScene(new Scene(root, w, h));
        stage.setResizable(false);
        stage.setFullScreenExitHint("Press esc to exit full screen");
        root.getStylesheets().add("styling.css");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}