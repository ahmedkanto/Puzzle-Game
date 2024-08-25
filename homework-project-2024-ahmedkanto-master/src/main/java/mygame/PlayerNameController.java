package mygame;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller for the player name input window.
 */
public class PlayerNameController {

    @FXML
    private TextField nameTextField;

    private BoardGameController boardGameController;

    /**
     * Sets the {@link BoardGameController} instance.
     *
     * @param boardGameController the {@link BoardGameController} instance to set
     */
    public void setBoardGameController(BoardGameController boardGameController) {
        this.boardGameController = boardGameController;
    }

    /**
     * Handles the submit action for the player name input.
     * Retrieves the player name from the text field, sets it in the {@link BoardGameController},
     * and closes the current window.
     */
    @FXML
    private void handleSubmit() {
        String playerName = nameTextField.getText().trim();
        if (!playerName.isEmpty()) {
            boardGameController.setPlayerName(playerName);
            Stage stage = (Stage) nameTextField.getScene().getWindow();
            stage.close();
        } else {
        }
    }
}
