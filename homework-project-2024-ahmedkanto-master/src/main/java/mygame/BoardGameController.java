package mygame;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelBoard.GameResult;
import modelBoard.LabyrinthModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller class for the Labyrinth puzzle game. Handles game logic, UI interactions, and high scores.
 */
public class BoardGameController {

    @FXML
    private GridPane board;

    @FXML
    private Label playerNameLabel;

    @FXML
    private Label movesLabel;

    @FXML
    private Label startTimeLabel;

    @FXML
    private TableView<GameResult> highScoresTable;

    @FXML
    private Button showHighScoresButton;

    @FXML
    private Button resetButton;

    @FXML
    private Button changeNameButton;

    private LabyrinthModel model;
    private String playerName;
    private Set<GameResult> highScores;
    private Timer timer;
    private static final String HIGH_SCORES_FILE = "highscores.json";
    private static final Logger logger = LogManager.getLogger(BoardGameController.class);

    /**
     * Initializes the game controller. Sets up the game board, high scores, and event handlers.
     */
    public void initialize() {
        model = new LabyrinthModel();
        highScores = new HashSet<>(loadHighScores());
        showPlayerNameWindow();
        startTimeLabel.setText("Start Time: " + model.getFormattedStartTime());

        setupHighScoresTable();
        drawBoard();
        updateLiveInfo();
        showHighScoresButton.setOnAction(event -> {
            toggleHighScoresTableVisibility();
            board.requestFocus();
        });
        resetButton.setOnAction(event -> {
            resetGame();
            showGiveUpAlert();
            board.requestFocus();
        });
        changeNameButton.setOnAction(event -> {
            handleChangeName();
            board.requestFocus();
        });
        startTimer();

        board.requestFocus();
        board.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case UP:
                case DOWN:
                case LEFT:
                case RIGHT:
                    handleKeyPress(event);
                    event.consume();
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * Starts the timer to update the moves count every second.
     */
    private void startTimer() {
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> movesLabel.setText("Moves: " + model.getMoves()));
            }
        }, 0, 1000);
    }

    /**
     * Displays a window for entering the player's name.
     */
    private void showPlayerNameWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PlayerName.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Player Name");
            stage.setScene(new Scene(loader.load()));
            stage.setWidth(400);
            stage.setHeight(300);
            stage.initModality(Modality.APPLICATION_MODAL);

            PlayerNameController controller = loader.getController();
            controller.setBoardGameController(this);

            stage.showAndWait();
        } catch (IOException e) {
            logger.error("Failed to load player name window", e);
        }
    }

    /**
     * Sets the player's name and updates the label.
     *
     * @param playerName the player's name
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
        playerNameLabel.setText("Player: " + playerName);
        logger.info("Player name set to {}", playerName);
    }

    /**
     * Handles the action of changing the player's name.
     */
    @FXML
    private void handleChangeName() {
        showPlayerNameWindow();
    }

    /**
     * Sets up the high scores table with the appropriate columns and data.
     */
    private void setupHighScoresTable() {
        highScoresTable.getColumns().clear();

        TableColumn<GameResult, Number> rankColumn = new TableColumn<>("Rank");
        rankColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(
                () -> highScoresTable.getItems().indexOf(cellData.getValue()) + 1, highScoresTable.getItems()
        ));

        TableColumn<GameResult, String> playerNameColumn = new TableColumn<>("Player");
        playerNameColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));

        TableColumn<GameResult, String> endTimeColumn = new TableColumn<>("Time");
        endTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFormattedDuration()));

        TableColumn<GameResult, Integer> movesColumn = new TableColumn<>("Moves");
        movesColumn.setCellValueFactory(new PropertyValueFactory<>("moves"));

        TableColumn<GameResult, Boolean> solvedColumn = new TableColumn<>("Solved");
        solvedColumn.setCellValueFactory(new PropertyValueFactory<>("solved"));

        highScoresTable.getColumns().addAll(rankColumn, playerNameColumn, endTimeColumn, movesColumn, solvedColumn);
        updateHighScoresTable();
        highScoresTable.setVisible(false);
    }

    /**
     * Draws the game board based on the current state of the model.
     */
    private void drawBoard() {
        board.getChildren().clear();
        board.setHgap(0);
        board.setVgap(0);
        for (int row = 0; row < model.getRows(); row++) {
            for (int col = 0; col < model.getCols(); col++) {
                Pane cellPane = new Pane();
                cellPane.setPrefSize(100, 100);
                cellPane.setStyle("-fx-border-color: black; -fx-background-color: white;");

                if (model.isBallPosition(row, col)) {
                    Circle ball = new Circle(50, Color.BLUE);
                    ball.setCenterX(50);
                    ball.setCenterY(50);
                    cellPane.getChildren().add(ball);
                }

                if (model.isTargetPosition(row, col)) {
                    Circle target = new Circle(50, Color.TRANSPARENT);
                    target.setStroke(Color.BLACK);
                    target.setCenterX(50);
                    target.setCenterY(50);
                    cellPane.getChildren().add(target);
                }

                if (model.hasHorizontalWall(row, col)) {
                    Line hWall = new Line(0, 100, 100, 100);
                    hWall.setStrokeWidth(11);
                    cellPane.getChildren().add(hWall);
                }

                if (model.hasVerticalWall(row, col)) {
                    Line vWall = new Line(100, 0, 100, 100);
                    vWall.setStrokeWidth(11);
                    cellPane.getChildren().add(vWall);
                }

                board.add(cellPane, col, row);
            }
        }
    }

    /**
     * Handles key press events to move the ball.
     *
     * @param event the key event
     */
    @FXML
    void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case UP:
                model.moveUp();
                break;
            case DOWN:
                model.moveDown();
                break;
            case LEFT:
                model.moveLeft();
                break;
            case RIGHT:
                model.moveRight();
                break;
            default:
                return;
        }
        drawBoard();
        checkGameStatus();
    }

    /**
     * Checks the game status to determine if the game is won or if the player has given up.
     */
    private void checkGameStatus() {
        if (model.isGameWon()) {
            timer.cancel();
            LocalDateTime endTime = LocalDateTime.now();
            Duration duration = Duration.between(model.getStartTime(), endTime);
            GameResult result = new GameResult(playerName, model.getStartTime(), endTime, model.getMoves(), true);
            highScores.add(result);
            saveHighScores();
            updateHighScoresTable();
            showVictoryAlert(duration);
            logger.info("Game won by {} in {} moves and {} duration", playerName, model.getMoves(), formatDuration(duration));
        } else if (resetButton.isPressed()) {
            resetGame();
            showGiveUpAlert();
            board.requestFocus();
            updateHighScoresTable();
        }
    }

    /**
     * Shows an alert dialog indicating that the player has won the game.
     *
     * @param duration the duration of the game
     */
    private void showVictoryAlert(Duration duration) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Victory!");
        alert.setHeaderText("You won the game!");
        alert.setContentText("Moves: " + model.getMoves() + "\nTime: " + formatDuration(duration));
        alert.showAndWait();
    }

    /**
     * Shows an alert dialog indicating that the player has given up the game.
     */
    private void showGiveUpAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over!");
        alert.setHeaderText("You lost the game!");
        alert.showAndWait();
    }

    /**
     * Formats the game duration as a string.
     *
     * @param duration the duration of the game
     * @return the formatted duration
     */
    private String formatDuration(Duration duration) {
        long minutes = duration.toMinutes();
        long seconds = duration.minusMinutes(minutes).getSeconds();
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Resets the game to the initial state.
     */
    @FXML
    private void resetGame() {
        timer.cancel();
        model = new LabyrinthModel();
        drawBoard();
        startTimeLabel.setText("Start Time: " + model.getFormattedStartTime());
        movesLabel.setText("Moves: 0");
        startTimer();
        board.requestFocus();
        logger.info("Game reset");
    }

    /**
     * Loads the high scores from a file.
     *
     * @return a list of game results
     */
    private List<GameResult> loadHighScores() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        File file = new File(HIGH_SCORES_FILE);
        if (file.exists()) {
            try {
                return mapper.readValue(file, new TypeReference<List<GameResult>>() {});
            } catch (IOException e) {
                logger.error("Failed to load high scores", e);
            }
        }
        return new ArrayList<>();
    }

    /**
     * Saves the high scores to a file.
     */
    private void saveHighScores() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            mapper.writeValue(new File(HIGH_SCORES_FILE), new ArrayList<>(getTopHighScores()));
        } catch (IOException e) {
            logger.error("Failed to save high scores", e);
        }
    }

    /**
     * Gets the top high scores.
     *
     * @return a list of the top high scores
     */
    private List<GameResult> getTopHighScores() {
        return highScores.stream()
                .sorted(Comparator.comparing(GameResult::getDuration)
                        .thenComparing(GameResult::getMoves)
                        .thenComparing(GameResult::getStartTime))
                .limit(10)
                .collect(Collectors.toList());
    }

    /**
     * Shows the high scores table.
     */
    @FXML
    private void showHighScores() {
        toggleHighScoresTableVisibility();
    }

    /**
     * Toggles the visibility of the high scores table.
     */
    private void toggleHighScoresTableVisibility() {
        highScoresTable.setVisible(!highScoresTable.isVisible());
    }

    /**
     * Updates the high scores table with the latest data.
     */
    private void updateHighScoresTable() {
        highScoresTable.setItems(FXCollections.observableArrayList(getTopHighScores()));
    }

    /**
     * Updates the live information displayed on the UI.
     */
    @FXML
    private void updateLiveInfo() {
        movesLabel.setText("Moves: " + model.getMoves());
    }

}
