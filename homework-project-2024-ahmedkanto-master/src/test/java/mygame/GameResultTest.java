package mygame;

import modelBoard.GameResult;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class GameResultTest {

    @Test
    void testGetDuration() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusMinutes(5).plusSeconds(30);
        GameResult result = new GameResult("Player", startTime, endTime, 10, true);
        Duration duration = result.getDuration();
        assertEquals(5, duration.toMinutes());
        assertEquals(30, duration.minusMinutes(5).getSeconds());
    }

    @Test
    void testGetFormattedDuration() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusMinutes(5).plusSeconds(30);
        GameResult result = new GameResult("Player", startTime, endTime, 10, true);
        String formattedDuration = result.getFormattedDuration();
        assertEquals("05:30", formattedDuration);
    }

    @Test
    void testCompareTo() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusMinutes(5).plusSeconds(30);
        GameResult result1 = new GameResult("Player1", startTime, endTime, 10, true);
        GameResult result2 = new GameResult("Player2", startTime, endTime, 15, true);

        assertTrue(result1.compareTo(result2) < 0);
    }
}
