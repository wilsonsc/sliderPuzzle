import org.junit.Test;

import scottnickamanda.sliderpuzzle.GameBoard;
import scottnickamanda.sliderpuzzle.Piece;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/***********************************************************************
 * JUnit Tests
 *
 * @author Scott Wilson
 **********************************************************************/
public class GameActivityTest {

    /** Initialize a new GameBoard */
    GameBoard board = new GameBoard(3);

    //Tests if game is in progress
    @Test
    public void testGame() {
        assertFalse(board.getGameProgress());
        board.setGameProgress(true);
        assertTrue(board.getGameProgress());
    }

    //Tests checking move validity
    @Test
    public void checkMoves() {
        board.setGameProgress(true);
        board.setBlankPiece(4);
        assertTrue(board.checkMove(3));
        assertTrue(board.checkMove(5));
        assertTrue(board.checkMove(1));
        assertTrue(board.checkMove(7));
        assertFalse(board.checkMove(4));
        assertFalse(board.checkMove(10));
    }

    //Tests game ending condition
    @Test
    public void checkIfGameOverA() {
        board.newGame();
        assertTrue(board.checkIfWon());
        assertFalse(board.getGameProgress());
    }

    //Tests false game ending condition
    @Test
    public void checkIfGameOverB() {
        board.newGame();
        board.movePieces(7);
        assertFalse(board.checkIfWon());
        assertTrue(board.getGameProgress());
    }

    //Tests move counter updates
    @Test
    public void testMoveCounter() {
        board.newGame();
        assertEquals(0, board.getMoveCounter());
        board.movePieces(5);
        assertEquals(1, board.getMoveCounter());
        board.movePieces(8);
        assertEquals(2, board.getMoveCounter());
        board.movePieces(10);
        assertEquals(2, board.getMoveCounter());
    }

    //Tests the pieces class
    @Test
    public void testPieces() {
        board.newGame();
        Piece[] testPieces = board.getPieces();
        assertEquals(1, testPieces[0].getNumber());
        assertEquals(2, testPieces[1].getNumber());
        board.movePieces(5);
        assertEquals(0, testPieces[5].getNumber());
        assertEquals(6, testPieces[8].getNumber());
        assertEquals("2", testPieces[1].toString());
    }

    //Tests alternate board size (4x4)
    @Test
    public void test4x4Board() {
        board = new GameBoard(4);
        board.newGame();
        assertEquals(16, board.getBoardSize());
        Piece[] testPieces = board.getPieces();
        assertEquals(16, testPieces.length);
        assertEquals("", testPieces[15].toString());
        assertEquals(0, testPieces[15].getNumber());
    }

    //Tests alternate board size (5x5)
    @Test
    public void test5x5Board() {
        board = new GameBoard(5);
        board.newGame();
        assertEquals(25, board.getBoardSize());
        Piece[] testPieces = board.getPieces();
        assertEquals(25, testPieces.length);
        assertEquals("", testPieces[24].toString());
        assertEquals(21, testPieces[20].getNumber());
    }
}