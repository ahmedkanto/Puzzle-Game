package modelBoard;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Represents the result of a game.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameResult implements Comparable<GameResult> {
    private String playerName;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;
    private int moves;
    private boolean solved;

    /**
     * Default constructor for GameResult.
     */
    public GameResult() {}

    /**
     * Constructs a GameResult with the specified parameters.
     *
     * @param playerName the name of the player
     * @param startTime the start time of the game
     * @param endTime the end time of the game
     * @param moves the number of moves made
     * @param solved whether the game was solved
     */
    public GameResult(String playerName, LocalDateTime startTime, LocalDateTime endTime, int moves, boolean solved) {
        this.playerName = playerName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.moves = moves;
        this.solved = solved;
    }

    /**
     * Gets the player name.
     *
     * @return the player name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Sets the player name.
     *
     * @param playerName the player name to set
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Gets the start time.
     *
     * @return the start time
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time.
     *
     * @param startTime the start time to set
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the end time.
     *
     * @return the end time
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time.
     *
     * @param endTime the end time to set
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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
     * Sets the number of moves made.
     *
     * @param moves the number of moves to set
     */
    public void setMoves(int moves) {
        this.moves = moves;
    }

    /**
     * Checks if the game was solved.
     *
     * @return true if the game was solved, false otherwise
     */
    public boolean isSolved() {
        return solved;
    }

    /**
     * Sets whether the game was solved.
     *
     * @param solved true if the game was solved, false otherwise
     */
    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    /**
     * Gets the duration of the game.
     *
     * @return the duration of the game
     */
    public Duration getDuration() {
        return Duration.between(startTime, endTime);
    }

    /**
     * Gets the formatted duration of the game.
     *
     * @return the formatted duration
     */
    public String getFormattedDuration() {
        Duration duration = getDuration();
        long minutes = duration.toMinutes();
        long seconds = duration.minusMinutes(minutes).getSeconds();
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Compares this GameResult to another GameResult.
     *
     * @param other the other GameResult to compare to
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object
     */
    @Override
    public int compareTo(GameResult other) {
        int durationCompare = getDuration().compareTo(other.getDuration());
        if (durationCompare != 0) {
            return durationCompare;
        }
        int movesCompare = Integer.compare(moves, other.moves);
        if (movesCompare != 0) {
            return movesCompare;
        }
        return startTime.compareTo(other.startTime);
    }
}
