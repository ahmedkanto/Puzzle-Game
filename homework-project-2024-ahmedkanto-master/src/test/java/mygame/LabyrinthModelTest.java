package mygame;

import modelBoard.LabyrinthModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class LabyrinthModelTest {

    private LabyrinthModel model;

    @BeforeEach
    void setUp() {
        model = new LabyrinthModel();
    }

    @Test
    void testInitialBallPosition() {
        assertTrue(model.isBallPosition(1, 4));
    }

    @Test
    void testMoveUp() {
        model.moveUp();
        assertTrue(model.isBallPosition(0, 4));
    }

    @Test
    void testMoveDown() {
        model.moveDown();
        assertTrue(model.isBallPosition(4, 4));
    }

    @Test
    void testMoveLeft() {
        model.moveLeft();
        assertTrue(model.isBallPosition(1, 0));
    }

    @Test
    void testMoveRight() {
        model.moveRight();
        assertTrue(model.isBallPosition(1, 6));
    }

    @Test
    void testIsGameWon() {
        model.moveRight();
        model.moveDown();
        model.moveLeft();
        model.moveDown();
        model.moveLeft();
        model.moveUp();
        model.moveLeft();
        model.moveDown();
        model.moveLeft();
        model.moveUp();
        model.moveRight();
        model.moveUp();
        model.moveRight();
        model.moveUp();
        model.moveLeft();
        model.moveDown();
        model.moveRight();
        model.moveDown();
        assertTrue(model.isGameWon());
    }

    @Test
    void testGetMoves() {
        model.moveRight();
        model.moveLeft();
        assertEquals(8, model.getMoves());
    }

    @Test
    void testGetStartTime() {
        LocalDateTime startTime = model.getStartTime();
        assertNotNull(startTime);
    }
}
