package modelBoard;

import puzzle.State;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents the model for the Labyrinth puzzle game.
 */
public class LabyrinthModel implements State<String>, Cloneable {
    private static final int ROWS = 7;
    private static final int COLS = 7;
    private int ballRow;
    private int ballCol;
    private final int targetRow = 5;
    private final int targetCol = 2;

    private int moves;
    private LocalDateTime startTime;

    private final boolean[][] horizontalWalls = {
            {false, false, true, false, false, false, true},
            {false, false, false, false, false, false, false},
            {false, true, false, false, false, false, false},
            {false, false, false, true, false, false, true},
            {true, false, false, false, true, false, false},
            {false, false, true, false, false, false, false},
            {false, false, false, false, false, false, false},
    };

    private final boolean[][] verticalWalls = {
            {true, false, false, true, false, false, false},
            {false, false, false, false, false, false, false},
            {false, false, true, false, false, true, false},
            {false, false, false, true, true, false, false},
            {false, false, false, false, false, false, false},
            {false, true, true, false, false, false, false},
            {false, false, false, true, false, true, false},
    };

    /**
     * Constructs a new LabyrinthModel with the initial position of the ball and the start time.
     */
    public LabyrinthModel() {
        ballRow = 1;
        ballCol = 4;
        moves = 0;
        startTime = LocalDateTime.now();
    }

    /**
     * Gets the formatted start time.
     *
     * @return the formatted start time
     */
    public String getFormattedStartTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return startTime.format(formatter);
    }

    /**
     * Gets the number of rows.
     *
     * @return the number of rows
     */
    public int getRows() {
        return ROWS;
    }

    /**
     * Gets the number of columns.
     *
     * @return the number of columns
     */
    public int getCols() {
        return COLS;
    }

    /**
     * Checks if there is a horizontal wall at the specified position.
     *
     * @param row the row index
     * @param col the column index
     * @return true if there is a horizontal wall, false otherwise
     */
    public boolean hasHorizontalWall(int row, int col) {
        return horizontalWalls[row][col];
    }

    /**
     * Checks if there is a vertical wall at the specified position.
     *
     * @param row the row index
     * @param col the column index
     * @return true if there is a vertical wall, false otherwise
     */
    public boolean hasVerticalWall(int row, int col) {
        return verticalWalls[row][col];
    }

    /**
     * Checks if the ball is at the specified position.
     *
     * @param row the row index
     * @param col the column index
     * @return true if the ball is at the specified position, false otherwise
     */
    public boolean isBallPosition(int row, int col) {
        return row == ballRow && col == ballCol;
    }

    /**
     * Checks if the target is at the specified position.
     *
     * @param row the row index
     * @param col the column index
     * @return true if the target is at the specified position, false otherwise
     */
    public boolean isTargetPosition(int row, int col) {
        return row == targetRow && col == targetCol;
    }

    /**
     * Moves the ball up.
     */
    public void moveUp() {
        while (ballRow > 0 && !horizontalWalls[ballRow - 1][ballCol]) {
            ballRow--;
            moves++;
        }
    }

    /**
     * Moves the ball down.
     */
    public void moveDown() {
        while (ballRow < ROWS - 1 && !horizontalWalls[ballRow][ballCol]) {
            ballRow++;
            moves++;
        }
    }

    /**
     * Moves the ball left.
     */
    public void moveLeft() {
        while (ballCol > 0 && !verticalWalls[ballRow][ballCol - 1]) {
            ballCol--;
            moves++;
        }
    }

    /**
     * Moves the ball right.
     */
    public void moveRight() {
        while (ballCol < COLS - 1 && !verticalWalls[ballRow][ballCol]) {
            ballCol++;
            moves++;
        }
    }

    /**
     * Checks if the game is won.
     *
     * @return true if the game is won, false otherwise
     */
    public boolean isGameWon() {
        return ballRow == targetRow && ballCol == targetCol;
    }

    /**
     * Gets the number of moves made.
     *
     * @return the number of moves
     */
    public int getMoves() {
        return moves;
    }

    /**
     * Gets the start time.
     *
     * @return the start time
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public boolean isSolved() {
        return isGameWon();
    }

    @Override
    public boolean isLegalMove(String move) {
        switch (move) {
            case "UP":
                return ballRow > 0 && !horizontalWalls[ballRow - 1][ballCol];
            case "DOWN":
                return ballRow < ROWS - 1 && !horizontalWalls[ballRow][ballCol];
            case "LEFT":
                return ballCol > 0 && !verticalWalls[ballRow][ballCol - 1];
            case "RIGHT":
                return ballCol < COLS - 1 && !verticalWalls[ballRow][ballCol];
            default:
                return false;
        }
    }

    @Override
    public void makeMove(String move) {
        switch (move) {
            case "UP":
                moveUp();
                break;
            case "DOWN":
                moveDown();
                break;
            case "LEFT":
                moveLeft();
                break;
            case "RIGHT":
                moveRight();
                break;
        }
    }

    @Override
    public Set<String> getLegalMoves() {
        Set<String> legalMoves = new HashSet<>();
        if (isLegalMove("UP")) legalMoves.add("UP");
        if (isLegalMove("DOWN")) legalMoves.add("DOWN");
        if (isLegalMove("LEFT")) legalMoves.add("LEFT");
        if (isLegalMove("RIGHT")) legalMoves.add("RIGHT");
        return legalMoves;
    }

    @Override
    public LabyrinthModel clone() {
        try {
            LabyrinthModel copy = (LabyrinthModel) super.clone();
            copy.startTime = LocalDateTime.from(startTime);
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LabyrinthModel)) return false;
        LabyrinthModel that = (LabyrinthModel) o;
        return ballRow == that.ballRow &&
                ballCol == that.ballCol &&
                targetRow == that.targetRow &&
                targetCol == that.targetCol &&
                moves == that.moves &&
                Objects.equals(startTime, that.startTime) &&
                Arrays.deepEquals(horizontalWalls, that.horizontalWalls) &&
                Arrays.deepEquals(verticalWalls, that.verticalWalls);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(ballRow, ballCol, targetRow, targetCol, moves, startTime);
        result = 31 * result + Arrays.deepHashCode(horizontalWalls);
        result = 31 * result + Arrays.deepHashCode(verticalWalls);
        return result;
    }
}
