package app.application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Controller {
    @FXML
    public Label outText, howPlay, lifes, debugging;
    @FXML
    public TextField letter;
    @FXML
    public Button submitButton, infoButton, resetButton, tipButton;

    private Stage stage;
    private Scene scene;
    public static String mysteryWord="ERROR";
    public static String guessedWord ="";
    public static int attempts=6;
    public static int randomLetter;
    public static boolean containsLetter=false;
    public static boolean started=false;
    public static boolean isLoaded=false;
    public StringBuilder sB = new StringBuilder();

    private TextFormatter.Change limitToSingleLetter(TextFormatter.Change change) {
        String text = change.getControlNewText();
        if (text.length() > 1) {
            return null;
        }
        change.setText(change.getText().toUpperCase());
        return change;
    }
    public void updateGuessedWord(String guessedLetter) {
        StringBuilder builder = new StringBuilder(guessedWord);

        for (int i = 0; i < mysteryWord.length(); i++) {
            if (guessedLetter.charAt(0) == mysteryWord.charAt(i)) {
                builder.setCharAt(i, guessedLetter.charAt(0));
                containsLetter = true;
            }
        }
        if (!containsLetter) attempts--;
        guessedWord = builder.toString();
    }

    private void winCheck() {
        if (attempts<=0 && debugging !=null) {
            submitButton.setDisable(true);
            letter.setEditable(false);
            debugging.setWrapText(true);
            tipButton.setDisable(true);
            infoButton.setDisable(true);
            debugging.setText("Oh no, you lost!");
            outText.setText(mysteryWord);
        }

        if (Objects.equals(guessedWord, mysteryWord) && debugging !=null) {
            submitButton.setDisable(true);
            letter.setEditable(false);
            tipButton.setDisable(true);
            infoButton.setDisable(true);
            debugging.setText("You won! ");
        }
    }

    public void initialize() {
        sB = new StringBuilder();
        if (letter!=null) {
            letter.setTextFormatter(new TextFormatter<>(this::limitToSingleLetter));
            letter.setOnKeyReleased(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    submitB();
                }
            });
        }
        if (!isLoaded) {
            try {
                List<String> wordsList = Files.readAllLines(new File("hangman.txt").toPath());
                if (!wordsList.isEmpty()) {
                    int randomIndex = new Random().nextInt(wordsList.size());
                    mysteryWord = wordsList.get(randomIndex).toUpperCase();

                    guessedWord = "";
                    attempts = 6;
                    containsLetter = false;
                    started = false;
                } else {
                    outText.setText("Something went wrong");
                }
            } catch (IOException e) {
                outText.setText(String.valueOf(e));
            }
        }
        if (howPlay != null) {
            File file = new File("info.txt");
            try (Scanner scanner = new Scanner(file)) {
                StringBuilder sb = new StringBuilder();
                while (scanner.hasNextLine()) {
                    sb.append(scanner.nextLine()).append("\n");
                }
                howPlay.setText(sb.toString());
            } catch (IOException e) {
                howPlay.setText("Failure while loading text");
            }
            howPlay.setWrapText(true);
        }

        for (int i = 0; i < mysteryWord.length(); i++) {
            if (guessedWord.length()<=mysteryWord.length() && !isLoaded) {
                sB.append("-");
                guessedWord=sB.toString();
            }
        }

        if (outText!= null) outText.setText(guessedWord);

        if (lifes!=null) lifes.setText("Attempts: "+attempts);
        isLoaded=true;
    }

    @FXML
    protected void submitB() {
        started = true;
        letter.setEditable(true);
        String guessedLetter = letter.getText().toUpperCase();
        if (!guessedLetter.isEmpty()) {
            updateGuessedWord(guessedLetter);
            outText.setText(guessedWord);
            lifes.setText("Attempts: " + attempts);

            if (!containsLetter && debugging != null) {
                debugging.setText("There is no letter " + guessedLetter);
            } else if (containsLetter && debugging != null) {
                debugging.setText(guessedLetter + " is in this word");
            }
        }

        containsLetter = false;
        letter.setText("");
        winCheck();
    }

    @FXML
    private void exitButton() {
        Main.stage.close();
    }

    @FXML
    protected void tip() {
        List<Integer> unguessedIndices = new ArrayList<>();

        for (int i = 0; i < guessedWord.length(); i++) {
            if (guessedWord.charAt(i) == '-') {
                unguessedIndices.add(i);
            }
        }

        if (!unguessedIndices.isEmpty()) {
            randomLetter = unguessedIndices.get(new Random().nextInt(unguessedIndices.size()));
            updateGuessedWord(String.valueOf(mysteryWord.charAt(randomLetter)));
            outText.setText(guessedWord);
        }
        winCheck();
    }


    @FXML
    protected void ResetGame() {
        isLoaded=false;
        started=false;
        containsLetter=false;
        submitButton.setDisable(false);
        infoButton.setDisable(false);
        tipButton.setDisable(false);
        guessedWord="";
        debugging.setText("Type a letter");
        letter.setEditable(true);
        initialize();
    }

    public void showInfo(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("informations.fxml")));
        root.getStylesheets().add("styling.css");
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root,600,500);
        stage.setScene(scene);
        stage.show();
    }

    public void backToGame(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("game.fxml")));
        root.getStylesheets().add("styling.css");
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root,600,500);
        stage.setScene(scene);
        stage.show();
    }
}
